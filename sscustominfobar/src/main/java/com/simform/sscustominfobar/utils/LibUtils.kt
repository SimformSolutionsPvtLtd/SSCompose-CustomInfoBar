package com.simform.sscustominfobar.utils

import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection
import com.simform.sscustominfobar.main.SSComposeInfoBarShapes
import com.simform.sscustominfobar.main.SSComposeInfoDuration

/**
 * Duration in milliseconds
 */
private const val DURATION_LONG = 10000L
private const val DURATION_SHORT = 4000L

/**
 * Internal function to create shape for [SSComposeInfoBar] based on the [SSComposeInfoBarDirection] provided.
 *
 * @param direction The [SSComposeInfoBarDirection] type provided by user.
 */
internal fun getShapeByDirection(direction: SSComposeInfoBarDirection) = when (direction) {
    SSComposeInfoBarDirection.Top -> SSComposeInfoBarShapes.roundedBottom
    SSComposeInfoBarDirection.Bottom -> SSComposeInfoBarShapes.roundedTop
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