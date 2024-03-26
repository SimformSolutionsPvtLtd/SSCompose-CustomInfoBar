package com.simform.sscustominfobar.utils


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * Custom modifier to enable swipe to dismiss
 *
 * @param onDismissed The callback to be called when the item has been successfully dismissed.
 * @return Modifier
 */
fun Modifier.swipeable(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    // Create a modifier for processing pointer input within the region of the modified element.
    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            // Create an infinite loop in a coroutine scope to detect any touch activity.
            while (true) {
                val pointerId =
                    awaitPointerEventScope { awaitFirstDown().id } // Observe for a touch event and if a touch is detected take its id.
                offsetX.stop()
                val velocityTracker = VelocityTracker()
                awaitPointerEventScope { // Then observe for a drag gesture for that specific touch id.
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            offsetX.snapTo(horizontalDragOffset) // Update the offsetX with horizontalDragOffset without any animation and also cancelling any on-going animation
                        }
                        velocityTracker.addPosition(change.uptimeMillis, change.position) // keep track of each position when dragging with timestamp
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                val velocity = velocityTracker.calculateVelocity().x
                // calculate target offset based on the velocity to show fling behaviour
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    // If the target offset after fling behaviour is less than the items width then animate back to normal position
                    if (targetOffsetX.absoluteValue <= size.width) {
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Else animate to the target offset and after the animation call the onDismissed
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
        // Apply the horizontal offset to the element.
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}