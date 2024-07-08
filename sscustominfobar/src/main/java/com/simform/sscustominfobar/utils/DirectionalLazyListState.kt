package com.simform.sscustominfobar.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlin.math.absoluteValue

/**
 * Scroll Threshold To reduce the show/hide behaviour when user scroll very little.
 */
internal const val SCROLL_THRESHOLD = 50

/**
 * Enum class for direction of Scroll.
 */
internal enum class ScrollDirection {
    SettledAtTop, SettleAfterUpScroll, SettledAfterDownScroll,
}

/**
 * Creates a [DirectionalLazyListState] that is remembered across compositions.
 *
 * @param lazyListState [LazyListState] that will be used internally in [DirectionalLazyListState].
 * @return [DirectionalLazyListState]
 */
@Composable
internal fun rememberDirectionalLazyListState(
    lazyListState: LazyListState,
    scrollThreshold: Int = SCROLL_THRESHOLD
): DirectionalLazyListState {
    return remember {
        DirectionalLazyListState(lazyListState, scrollThreshold)
    }
}

/**
 * Class that extends the functionality of [LazyListState] by providing an extra observable variable scrollDirection
 * of type [ScrollDirection].
 *
 * @property lazyListState of type [LazyListState].
 */
internal class DirectionalLazyListState(
    private val lazyListState: LazyListState,
    private var scrollThreshold: Int = SCROLL_THRESHOLD
) {
    private var positionY = lazyListState.firstVisibleItemScrollOffset
    private var visibleItem = lazyListState.firstVisibleItemIndex
    private var scrollPosition = ScrollDirection.SettledAtTop

    internal fun updateScrollThreshold(threshold: Int) {
        scrollThreshold = threshold
    }

    val scrollDirection by derivedStateOf {
        if (lazyListState.isScrollInProgress.not()) {
            scrollPosition
        } else {
            val firstVisibleItemIndex = lazyListState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset =
                lazyListState.firstVisibleItemScrollOffset

//          We are scrolling while first visible item hasn't changed yet
            if (firstVisibleItemIndex == visibleItem) {
                val direction = if (firstVisibleItemScrollOffset > positionY) {
                    // Only update the value when the scroll has passed the threshold value.
                    if ((firstVisibleItemScrollOffset - positionY).absoluteValue > scrollThreshold) {
                        // User scrolls downward
                        scrollPosition =
                            ScrollDirection.SettledAfterDownScroll
                        positionY = firstVisibleItemScrollOffset
                    }
                    scrollPosition
                } else {
                    // User scrolls upward
                    if ((firstVisibleItemScrollOffset - positionY).absoluteValue > scrollThreshold) {
                        scrollPosition =
                            ScrollDirection.SettleAfterUpScroll
                        positionY = firstVisibleItemScrollOffset
                    }
                    scrollPosition
                }
                direction
            } else {
//          We are scrolling and the very first item is out of the screen.
                val direction = if (firstVisibleItemIndex > visibleItem) {
                    scrollPosition =
                        ScrollDirection.SettledAfterDownScroll
                    ScrollDirection.SettledAfterDownScroll
                } else {
                    scrollPosition =
                        ScrollDirection.SettleAfterUpScroll
                    ScrollDirection.SettleAfterUpScroll
                }
                positionY = firstVisibleItemScrollOffset
                visibleItem = firstVisibleItemIndex
                direction
            }
        }
    }
}