package com.simform.sscustominfobar.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simform.sscustominfobar.R
import com.simform.sscustominfobar.animation.AnimationType
import com.simform.sscustominfobar.animation.DefaultAnimationDuration
import com.simform.sscustominfobar.animation.ExtraDelayForNewInfoBar
import com.simform.sscustominfobar.animation.getAnimatedOffset
import com.simform.sscustominfobar.animation.getEnterAnimation
import com.simform.sscustominfobar.animation.getExitAnimation
import com.simform.sscustominfobar.defaultInfoBars.OfflineInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection.Bottom
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection.Top
import com.simform.sscustominfobar.main.SSComposeInfoBarShapes.roundedBottom
import com.simform.sscustominfobar.main.SSComposeInfoBarShapes.roundedTop
import com.simform.sscustominfobar.main.SSComposeInfoBarState.Hidden
import com.simform.sscustominfobar.main.SSComposeInfoBarState.Visible
import com.simform.sscustominfobar.main.SSComposeInfoDuration.Indefinite
import com.simform.sscustominfobar.main.SSComposeInfoDuration.Long
import com.simform.sscustominfobar.main.SSComposeInfoDuration.Short
import com.simform.sscustominfobar.res.Dimens
import com.simform.sscustominfobar.res.Dimens.DpEighty
import com.simform.sscustominfobar.res.Dimens.DpMedium
import com.simform.sscustominfobar.res.Dimens.DpSmall
import com.simform.sscustominfobar.res.Dimens.DpTwelve
import com.simform.sscustominfobar.res.Dimens.DpZero
import com.simform.sscustominfobar.utils.ConnectivityObserver
import com.simform.sscustominfobar.utils.DirectionalLazyListState
import com.simform.sscustominfobar.utils.SCROLL_THRESHOLD
import com.simform.sscustominfobar.utils.ScrollDirection
import com.simform.sscustominfobar.utils.TextType
import com.simform.sscustominfobar.utils.getShapeByDirection
import com.simform.sscustominfobar.utils.rememberDirectionalLazyListState
import com.simform.sscustominfobar.utils.swipeable
import com.simform.sscustominfobar.utils.toMillis
import com.simform.sscustominfobar.utils.toTextType
import kotlinx.coroutines.delay
import java.util.LinkedList
import java.util.Queue

/**
 * Max num of lines for description and title in [SSComposeInfoBar]
 */
private const val DESC_MAX_LINE = 2
private const val TITLE_MAX_LINE = 2

/**
 * Wrapper data class for information that will be displayed in [SSComposeInfoBar].
 *
 * @property title
 * @property description
 * @property icon
 */
data class SSComposeInfoBarData(
    val title: TextType,
    val description: TextType? = null,
    val icon: ImageVector = Icons.Default.Info
)

/**
 * Enum class that represents the 3 types of duration of [SSComposeInfoBar]
 * @property Short 4 seconds
 * @property Long 10 seconds
 * @property Indefinite [SSComposeInfoBar] won't be dismissed until dismissed by user.
 */
enum class SSComposeInfoDuration {
    Short,
    Long,
    Indefinite
}

/**
 * Enum class that represents the two states of [SSComposeInfoBar]
 * - Visible
 * - Hidden
 *
 * @property value Boolean value that is wrapped by this enum class.
 * (true for [Visible] and false for [Hidden])
 */
enum class SSComposeInfoBarState(val value: Boolean) {
    Visible(true), Hidden(false)
}

/**
 * Enum class that represents the direction from which the [SSComposeInfoBar] will be presented.
 * @property Top
 * @property Bottom
 */
enum class SSComposeInfoBarDirection(internal val alignment: Alignment) {
    Top(Alignment.TopCenter), Bottom(Alignment.BottomCenter)
}

/**
 * State of the [SSComposeInfoHost], which controls the current [SSComposeInfoBar] being shown
 * inside the [SSComposeInfoHost].
 *
 * This state is usually [remember]ed and used to provide to a [SSComposeInfoHost].
 */
class SSComposeInfoHostState {
    internal var previousState = Hidden
    internal var onDismissCallback: (() -> Unit)? = null
    fun setOnInfoBarDismiss(callback: () -> Unit) {
        onDismissCallback = callback
    }

    // TODO: If everything goes fine add one parameter in the class's constructor for the queue
    //  size and then use that here to create a fixed size linked list.
    private var infoBarDataQueue: Queue<SSComposeInfoBarData> = LinkedList()
    private var isQueueBeingProcessed = false

    /**
     * [MutableTransitionState] which is used internally by [SSComposeInfoHost] to show and hide [SSComposeInfoBar]
     */
    internal var visibilityState = MutableTransitionState(Hidden.value)

    private var _direction = mutableStateOf(Top)
    val direction: State<SSComposeInfoBarDirection> = _direction

    /**
     * Function that is used in [SSComposeInfoHost] internally to set the direction given by the user.
     */
    internal fun setDirection(direction: SSComposeInfoBarDirection) {
        this._direction.value = direction
    }

    /**
     * Private backing property for isInfinite.
     */
    internal var _isInfinite = mutableStateOf(false)

    /**
     * A read only [State] property of type [Boolean] which is used to represent whether the current [SSComposeInfoBar]'s Duration is Infinite or not.
     */
    val isInfinite: State<Boolean> = _isInfinite

    /**
     * A read only property that represents whether a [SSComposeInfoBar] is currently being shown or not.
     */
    val isVisible: Boolean
        get() = visibilityState.currentState == Visible.value

    /**
     * Private backing property for [currentComposeInfoBarData].
     */
    private var _currentComposeInfoBarData: MutableState<SSComposeInfoBarData?> =
        mutableStateOf(null)

    /**
     * A read only property that represents the data(title, description and icon) that should be displayed in the [SSComposeInfoBar].
     */
    val currentComposeInfoBarData: State<SSComposeInfoBarData?> = _currentComposeInfoBarData

    /**
     * Private backing property for offline [currentComposeInfoBarData].
     */
    private var _offlineInfoBarData: MutableState<SSComposeInfoBarData?> = mutableStateOf(null)
    
    /**
     * A read only property that represents the data(title, description and icon) 
     * that should be displayed in the offline [SSComposeInfoBar].
     */
    val offlineInfoBarData: State<SSComposeInfoBarData?> = _offlineInfoBarData

    fun setOfflineInfoBarData(offlineInfoBarData: SSComposeInfoBarData) {
        _offlineInfoBarData.value = offlineInfoBarData
    }

    /**
     * Function that is used to manually hide the currently displayed [SSComposeInfoBar].
     */
    fun dismiss() {
        visibilityState.targetState = Hidden.value
        previousState = Visible
    }

    /**
     * Scroll threshold to use for scroll to hide feature.
     */
    private var scrollThreshold = SCROLL_THRESHOLD

    /**
     * setter function for scrollThreshold.
     *
     * @param threshold
     */
    fun setScrollThreshold(threshold: Int) {
        scrollThreshold = threshold
        directionalLazyListState?.updateScrollThreshold(scrollThreshold)
    }

    // For Scroll-to-hide feature
    internal var directionalLazyListState: DirectionalLazyListState? = null

    // Initialize the directionalLazyListState using contentScrollState
    @Composable
    internal fun InitializeDirectionalLazyListState(contentScrollState: LazyListState?) {
        if (contentScrollState != null && directionalLazyListState == null) {
            directionalLazyListState =
                rememberDirectionalLazyListState(
                    lazyListState = contentScrollState,
                    scrollThreshold = scrollThreshold
                )
        }
    }

    /**
     * This suspend function is used to show [SSComposeInfoBar] in [SSComposeInfoHost].
     *
     * @param infoBarData [SSComposeInfoBarData] which will be used to display content(title, description) in [SSComposeInfoBar].
     * @param duration [SSComposeInfoDuration] which will determine how long the [SSComposeInfoBar] stays visible.
     */
    suspend fun show(
        infoBarData: SSComposeInfoBarData,
        duration: SSComposeInfoDuration
    ) {
        if (duration == Indefinite) {
            if (!isVisible) {
                _currentComposeInfoBarData.value = infoBarData
                _isInfinite.value = true
                visibilityState.targetState = Visible.value
                previousState = Hidden
            }
        } else {
            if (!isInfinite.value) {
                infoBarDataQueue.add(infoBarData)
                if (!isQueueBeingProcessed) {
                    isQueueBeingProcessed = true
                    while (infoBarDataQueue.isNotEmpty()) {
                        _currentComposeInfoBarData.value = infoBarDataQueue.remove()
                        visibilityState.targetState = Visible.value
                        // Wait for the given duration
                        delay(duration.toMillis())
                        visibilityState.targetState = Hidden.value
                        previousState = Visible
                        // Here we are using the default value of exit animation but when we will give custom animations we will have to use that duration.
                        // Store that exit animation in the SSComposeHostState.
                        delay(DefaultAnimationDuration.toLong() + ExtraDelayForNewInfoBar)
                    }
                    isQueueBeingProcessed = false
                }
            }
        }
    }
}

/**
 * Contains the default values used by [SSComposeInfoBar].
 */
object SSComposeInfoBarDefaults {
    /**
     * Default horizontalPadding and verticalPadding.
     */
    private val ComposeInfoBarHorizontalPadding = DpMedium
    private val ComposeInfoBarVerticalPadding = DpSmall

    /**
     * Default action title
     */
    val defaultActionTitle = "Action"

    /**
     * Default content padding for [SSComposeInfoBar].
     */
    val contentPadding = PaddingValues(
        start = ComposeInfoBarHorizontalPadding,
        end = ComposeInfoBarHorizontalPadding,
        top = ComposeInfoBarVerticalPadding,
        bottom = ComposeInfoBarVerticalPadding
    )

    /**
     * Default height of [SSComposeInfoBar].
     */
    internal val defaultHeight = DpEighty

    /**
     * Default Max lines for description and title in [SSComposeInfoBar].
     */
    internal const val descriptionMaxLine = DESC_MAX_LINE
    internal const val titleMaxLine = TITLE_MAX_LINE

    /**
     * Default [Shape] of [SSComposeInfoBar].
     */
    val shape: Shape = roundedBottom

    /**
     * Creates default [SSComposeInfoBarColors] for [SSComposeInfoBar].
     */
    val colors: SSComposeInfoBarColors
        @Composable get() = SSComposeInfoBarColors(
            iconColor = MaterialTheme.colorScheme.onPrimary,
            titleColor = MaterialTheme.colorScheme.onPrimary,
            descriptionColor = MaterialTheme.colorScheme.onPrimary,
            dismissIconColor = MaterialTheme.colorScheme.onPrimary,
            actionButtonColors = ButtonDefaults.elevatedButtonColors()
        )

    /**
     * Default [SSCustomBackground] for [SSComposeInfoBar].
     */
    val defaultSSCustomBackground @Composable get() = MaterialTheme.colorScheme.primary.toSSCustomBackground()

    /**
     * Creates default [SSComposeInfoBarElevation] for [SSComposeInfoBar]
     */
    val elevations =
        SSComposeInfoBarElevation(tonalElevation = DpSmall, shadowElevation = DpSmall)

    /**
     * Default title style of [SSComposeInfoBar].
     */
    val defaultTitleStyle
        @Composable get() = LocalTextStyle.current.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = Dimens.SpEighteen
        )

    /**
     * Default description style of [SSComposeInfoBar].
     */
    val defaultDescriptionStyle
        @Composable get() = LocalTextStyle.current.copy(
            fontWeight = FontWeight.Light,
            fontSize = Dimens.SpFourteen
        )
}

/**
 * Object that contains the shapes for [SSComposeInfoBar] depending upon the [SSComposeInfoBarDirection].
 *
 * @property roundedBottom Used when the [SSComposeInfoBarDirection] is [SSComposeInfoBarDirection.Top]
 * @property roundedTop Used when the [SSComposeInfoBarDirection] is [SSComposeInfoBarDirection.Bottom]
 */
object SSComposeInfoBarShapes {
    val roundedBottom =
        RoundedCornerShape(
            topStart = DpZero,
            topEnd = DpZero,
            bottomStart = DpTwelve,
            bottomEnd = DpTwelve
        )
    val roundedTop =
        RoundedCornerShape(
            topStart = DpTwelve,
            topEnd = DpTwelve,
            bottomStart = DpZero,
            bottomEnd = DpZero
        )
}

/**
 * Host for [SSComposeInfoBar]s to properly show, hide and dismiss items base on [SSComposeInfoHostState].
 *
 * @param modifier The Modifier to be applied to [SSComposeInfoHost].
 * @param composeHostState The state of the current [SSComposeInfoHost].
 * @param direction The direction from which the [SSComposeInfoBar] will be shown.
 * @param animationType The Animation with the [SSComposeInfoBar] will be shown.
 * @param contentScrollState The LazyListState which wil be used to show and hide the [SSComposeInfoBar] on scrolling.
 * @param enableNetworkMonitoring The flag that will decide whether the network monitoring feature is enabled or not.
 * @param composeInfoBar The [SSComposeInfoBar] that will be displayed in [SSComposeInfoHost].
 * @param content content of the screen on which the [SSComposeInfoBar] will be shown.
 */
@Composable
fun SSComposeInfoHost(
    modifier: Modifier = Modifier,
    composeHostState: SSComposeInfoHostState,
    direction: SSComposeInfoBarDirection = Top,
    animationType: AnimationType = AnimationType.SlideVertically,
    contentScrollState: LazyListState? = null,
    enableNetworkMonitoring: Boolean = false,
    isSwipeToDismissEnabled: Boolean = false,
    composeInfoBar: @Composable (SSComposeInfoBarData) -> Unit,
    content: @Composable () -> Unit
) {
    composeHostState.setDirection(direction)
    composeHostState.InitializeDirectionalLazyListState(contentScrollState = contentScrollState)
    val exitAnimation = getExitAnimation(composeHostState.direction.value, animationType)
    val enterAnimation = getEnterAnimation(composeHostState.direction.value, animationType)

    // For dismiss callback
    LaunchedEffect(key1 = composeHostState.isVisible) {
        // Here we are checking whether the info bar was first visible and then it went into dismissed state
        // This will help in when we don't want the callback to be called initially when the infoBar is not visible and the launched is called initially.
        if (composeHostState.previousState == Visible && !composeHostState.isVisible) {
            composeHostState.onDismissCallback?.let { it() }
            if (composeHostState.isInfinite.value) {
                composeHostState._isInfinite.value = false
            }
        }
    }

    // For network monitoring
    val context = LocalContext.current
    val monitor = remember { ConnectivityObserver(context) }
    var isOnline: State<Boolean>? = null
    if (enableNetworkMonitoring) {
        isOnline = monitor.isOnline.collectAsStateWithLifecycle(initialValue = true)
    }
    val shouldBeVisible by remember(composeHostState.isVisible) {
        derivedStateOf {
            if (composeHostState.isVisible) {
                // The scroll to hide behaviour should only be allowed when a SSInfoBar is currently being shown.
                (composeHostState.directionalLazyListState?.scrollDirection == ScrollDirection.SettledAtTop
                        || composeHostState.directionalLazyListState?.scrollDirection == ScrollDirection.SettleAfterUpScroll)
            } else true
        }
    }
    val animatedYOffset by getAnimatedOffset(
        shouldBeVisible = shouldBeVisible,
        direction = direction
    )
    Box(
        modifier = modifier
    ) {
        content()
        AnimatedVisibility(
            visibleState = composeHostState.visibilityState,
            modifier = Modifier
                .align(composeHostState.direction.value.alignment)
                .then(
                    if (isSwipeToDismissEnabled && composeHostState.isInfinite.value) Modifier.swipeable { composeHostState.dismiss() } else Modifier
                )
                .then(
                    if (composeHostState.directionalLazyListState != null) Modifier
                        .graphicsLayer {
                            translationY = animatedYOffset
                        } else Modifier
                ),
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            composeHostState.currentComposeInfoBarData.value?.let { content ->
                composeInfoBar(content)
            }
        }
        if (enableNetworkMonitoring) {
            isOnline?.value?.let {
                AnimatedVisibility(
                    visible = !it,
                    modifier = Modifier
                        .align(composeHostState.direction.value.alignment),
                    enter = enterAnimation,
                    exit = exitAnimation
                ) {
                    val offlineInfoBarData = composeHostState.offlineInfoBarData.value
                    if (offlineInfoBarData != null) {
                        OfflineInfoBar(offlineData = offlineInfoBarData)
                    } else {
                        OfflineInfoBar(
                            offlineData = SSComposeInfoBarData(
                                title = stringResource(R.string.offline_info_bar_title).toTextType(),
                                description = stringResource(R.string.offline_info_bar_description).toTextType()
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Host for [SSComposeInfoBar]s to properly show, hide and dismiss items base on [SSComposeInfoHostState].
 *
 * Note: This SSComposeInfoHost does not provide a way to show custom [SSComposeInfoBar]. To provide custom [SSComposeInfoBar] checkout other SSComposeInfoHost overloads.
 *
 * @param modifier The Modifier to be applied to [SSComposeInfoHost].
 * @param composeHostState The state of the current [SSComposeInfoHost].
 * @param direction The direction from which the [SSComposeInfoBar] will be shown.
 * @param animationType The Animation with the [SSComposeInfoBar] will be shown.
 * @param contentScrollState The LazyListState which wil be used to show and hide the [SSComposeInfoBar] on scrolling.
 * @param enableNetworkMonitoring The flag that will decide whether the network monitoring feature is enabled or not.
 * @param content content of the screen on which the [SSComposeInfoBar] will be shown.
 */
@Composable
fun SSComposeInfoHost(
    modifier: Modifier = Modifier,
    composeHostState: SSComposeInfoHostState,
    direction: SSComposeInfoBarDirection = Top,
    animationType: AnimationType = AnimationType.SlideVertically,
    contentScrollState: LazyListState? = null,
    enableNetworkMonitoring: Boolean = false,
    isSwipeToDismissEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    composeHostState.setDirection(direction)
    composeHostState.InitializeDirectionalLazyListState(contentScrollState = contentScrollState)
    val exitAnimation = getExitAnimation(composeHostState.direction.value, animationType)
    val enterAnimation = getEnterAnimation(composeHostState.direction.value, animationType)

    // For dismiss callback
    LaunchedEffect(key1 = composeHostState.isVisible) {
        // Here we are checking whether the info bar was first visible and then it went into dismissed state
        // This will help in when we don't want the callback to be called initially when the infoBar is not visible and the launched is called initially.
        if (composeHostState.previousState == Visible && !composeHostState.isVisible) {
            composeHostState.onDismissCallback?.let { it() }
            if (composeHostState.isInfinite.value) {
                composeHostState._isInfinite.value = false
            }
        }
    }

    // For network monitoring
    val context = LocalContext.current
    val monitor = remember { ConnectivityObserver(context) }
    var isOnline: State<Boolean>? = null
    if (enableNetworkMonitoring) {
        isOnline = monitor.isOnline.collectAsStateWithLifecycle(initialValue = true)
    }

    val shouldBeVisible by remember(composeHostState.isVisible) {
        derivedStateOf {
            if (composeHostState.isVisible) {
                // The scroll to hide behaviour should only be allowed when a SSInfoBar is currently being shown.
                (composeHostState.directionalLazyListState?.scrollDirection == ScrollDirection.SettledAtTop
                        || composeHostState.directionalLazyListState?.scrollDirection == ScrollDirection.SettleAfterUpScroll)
            } else true
        }
    }
    val animatedYOffset by getAnimatedOffset(
        shouldBeVisible = shouldBeVisible,
        direction = direction
    )
    Box(
        modifier = modifier
    ) {
        content()
        AnimatedVisibility(
            visibleState = composeHostState.visibilityState,
            modifier = Modifier
                .align(composeHostState.direction.value.alignment)
                .then(
                    if (isSwipeToDismissEnabled && composeHostState.isInfinite.value) Modifier.swipeable { composeHostState.dismiss() } else Modifier
                )
                .then(
                    if (composeHostState.directionalLazyListState != null) Modifier
                        .graphicsLayer {
                            translationY = animatedYOffset
                        } else Modifier
                ),
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            composeHostState.currentComposeInfoBarData.value?.let { infoBarData ->
                SSComposeInfoBar(
                    title = infoBarData.title,
                    description = infoBarData.description,
                    shape = getShapeByDirection(composeHostState.direction.value),
                    isInfinite = composeHostState.isInfinite.value,
                    onCloseClicked = {
                        composeHostState.dismiss()
                    }
                )
            }
        }
        if (enableNetworkMonitoring) {
            isOnline?.value?.let {
                AnimatedVisibility(
                    visible = !it,
                    modifier = Modifier
                        .align(composeHostState.direction.value.alignment),
                    enter = enterAnimation,
                    exit = exitAnimation
                ) {
                    val offlineInfoBarData = composeHostState.offlineInfoBarData.value
                    if (offlineInfoBarData != null) {
                        OfflineInfoBar(offlineData = offlineInfoBarData)
                    } else {
                        OfflineInfoBar(
                            offlineData = SSComposeInfoBarData(
                                title = stringResource(R.string.offline_info_bar_title).toTextType(),
                                description = stringResource(R.string.offline_info_bar_description).toTextType()
                            )
                        )
                    }
                }
            }
        }
    }
}