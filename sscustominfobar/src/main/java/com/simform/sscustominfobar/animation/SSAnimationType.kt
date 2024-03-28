package com.simform.sscustominfobar.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import com.simform.sscustominfobar.animation.SSAnimationType.SlideInFromBottom
import com.simform.sscustominfobar.animation.SSAnimationType.SlideInFromTop

private const val DefaultAnimationDuration = 600

/**
 * Enum values to represent animation type of info bar showing and leaving
 * @property SlideInFromBottom
 * @property SlideInFromTop
 **/
enum class SSAnimationType {
    SlideInFromTop, SlideInFromBottom
}

/**
 * Internal function to get enter animation of compose info bar based on [animationType] provided
 *
 * Example: If the animation type if from top to bottom then we will get slideInVertically with initial offset from top
 * and if the animation type if from bottom to top then the initial offset will be set to outside of bottom of the screen
 * @param animationType type of animation
 * @param duration duration of animation in milliseconds
 */
internal fun getEnterAnimation(animationType: SSAnimationType, duration: Int = DefaultAnimationDuration) =
    slideInVertically(tween(duration), initialOffsetY = { height ->
        if (animationType == SlideInFromTop) {
            -height
        } else {
            height
        }
    })

/**
 * Internal function to get exit animation of compose info bar based on [animationType] provided
 *
 * Example: If the animation type if from top to bottom then we will get slideInVertically with target offset from top
 * and if the animation type if from bottom to top then the target offset will be set to outside of bottom of the screen
 * @param animationType type of animation
 * @param duration duration of animation in milliseconds
 */
internal fun getExitAnimation(animationType: SSAnimationType, duration: Int = DefaultAnimationDuration) =
    slideOutVertically(tween(duration), targetOffsetY = { height ->
        if (animationType == SlideInFromTop) {
            -height
        } else {
            height
        }
    })