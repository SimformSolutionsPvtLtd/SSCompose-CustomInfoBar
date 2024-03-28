package com.simform.sscustominfobar.utils

import androidx.compose.ui.Alignment
import com.simform.sscustominfobar.animation.SSAnimationType
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarShapes
import com.simform.sscustominfobar.main.SSComposeInfoConfig
import com.simform.sscustominfobar.main.SSComposeInfoDuration

/**
 * Duration in milliseconds
 */
private const val DURATION_LONG = 10000L
private const val DURATION_SHORT = 4000L

/**
 * Internal function to create [SSComposeInfoConfig] based on the animationType provided.
 * [SSComposeInfoConfig] contains configuration information such as the shape of the [SSComposeInfoBar] and The position (Top or Bottom) of the [SSComposeInfoBar].
 *
 * @param animationType The Animation type provided by user.
 */
internal fun getComposeInfoConfigs(animationType: SSAnimationType) = when (animationType) {
    SSAnimationType.SlideInFromTop -> SSComposeInfoConfig(
        position = Alignment.TopCenter,
        shape = SSComposeInfoBarShapes.roundedBottom
    )

    SSAnimationType.SlideInFromBottom -> SSComposeInfoConfig(
        position = Alignment.BottomCenter,
        shape = SSComposeInfoBarShapes.roundedTop
    )
}

/**
 * Internal util function to get Duration of [SSComposeInfoBar] based on [SSComposeInfoDuration].
 *
 * @return Durations in milli Seconds.
 */
internal fun SSComposeInfoDuration.toMillis(): Long {
    val original = when (this) {
        SSComposeInfoDuration.Indefinite -> Long.MAX_VALUE
        SSComposeInfoDuration.Long -> DURATION_LONG
        SSComposeInfoDuration.Short -> DURATION_SHORT
    }
    return original
}