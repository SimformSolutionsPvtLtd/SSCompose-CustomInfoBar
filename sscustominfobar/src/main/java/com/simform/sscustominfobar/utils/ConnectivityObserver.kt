package com.simform.sscustominfobar.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

internal interface NetworkObserver {
    val isOnline: Flow<Boolean>
}

/**
 * Connectivity observer class that can be used to observe the internet status.
 *
 * @constructor
 * [Context]
 */
class ConnectivityObserver(context: Context) : NetworkObserver {

    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        // Callback for network changes
        val callback = object : ConnectivityManager.NetworkCallback() {
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                channel.trySend(networks.isNotEmpty())
            }
        }

        val request =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Initially checks the internet when the app is launched and this class is created.
        channel.trySend(connectivityManager.isInternetAvailable())

        // When the flow is removed from the composition it unregisters the network callback.
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged().conflate()
}

private fun ConnectivityManager.isInternetAvailable(): Boolean {
    var result = false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getNetworkCapabilities(activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    } else {
        activeNetworkInfo?.run {
            if (type == ConnectivityManager.TYPE_WIFI) {
                result = true
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                result = true
            }
        }
    }
    return result
}