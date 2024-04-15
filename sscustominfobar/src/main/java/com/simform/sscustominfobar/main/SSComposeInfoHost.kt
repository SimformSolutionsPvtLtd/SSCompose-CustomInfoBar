package com.simform.sscustominfobar.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simform.sscustominfobar.R
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
import com.simform.sscustominfobar.utils.ScrollDirection
import com.simform.sscustominfobar.utils.TextType
import com.simform.sscustominfobar.utils.getShapeByDirection
import com.simform.sscustominfobar.utils.rememberDirectionalLazyListState
import com.simform.sscustominfobar.utils.swipeable
import com.simform.sscustominfobar.utils.toMillis
import com.simform.sscustominfobar.utils.toTextType
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
 */
data class SSComposeInfoBarData(
    val title: TextType,
    val description: TextType? = null
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
    private var _isInfinite = mutableStateOf(false)

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
     * Function that is used to manually hide the currently displayed [SSComposeInfoBar].
     */
    fun dismiss() {
        visibilityState.targetState = Hidden.value
    }

    /**
     * A private suspend function that is used to toggle the [visibilityState] based on the input parameters.
     *
     * @param isTimed Optional Flag that denotes whether the [SSComposeInfoBar] is Visible for only some time or Visible Infinitely.
     * @param duration Optional [SSComposeInfoDuration] that will be used to show [SSComposeInfoBar] for that amount of time.
     */
    private suspend fun toggleVisibility(
        isTimed: Boolean = false,
        duration: SSComposeInfoDuration = Indefinite
    ) {
        if (isTimed) {
            // Executes this if the visibility duration is either Long or Short and not Indefinite.
            coroutineScope {
                launch {
                    _isInfinite.value = false
                    visibilityState.targetState = Visible.value
                    // Wait for the given duration
                    delay(duration.toMillis())
                    visibilityState.targetState = Hidden.value
                }
            }
            return
        }
        // Executes this if the visibility duration is Indefinite
        _isInfinite.value = true
        visibilityState.targetState = Visible.value
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
        // This checks whether the current SSComposeInfoBar is not visible and the visibility state is idle(not running any animation).
        if (!isVisible && visibilityState.isIdle) {
            _currentComposeInfoBarData.value = infoBarData
            if (duration == Indefinite) {
                toggleVisibility()
            } else {
                toggleVisibility(isTimed = true, duration = duration)
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
    internal val descriptionMaxLine = DESC_MAX_LINE
    internal val titleMaxLine = TITLE_MAX_LINE

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
            dismissIconColor = MaterialTheme.colorScheme.onPrimary
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
    contentScrollState: LazyListState? = null,
    enableNetworkMonitoring: Boolean = false,
    composeInfoBar: @Composable (SSComposeInfoBarData) -> Unit,
    content: @Composable () -> Unit
) {
    composeHostState.setDirection(direction)
    val exitAnimation = getExitAnimation(composeHostState.direction.value)
    val enterAnimation = getEnterAnimation(composeHostState.direction.value)

    // For network monitoring
    val context = LocalContext.current
    val monitor = remember { ConnectivityObserver(context) }
    var isOnline: State<Boolean>? = null
    if (enableNetworkMonitoring) {
        isOnline = monitor.isOnline.collectAsStateWithLifecycle(initialValue = true)
    }

    // For Scroll-to-hide feature
    var directionalLazyListState: DirectionalLazyListState? = null
    if (contentScrollState != null) {
        directionalLazyListState =
            rememberDirectionalLazyListState(lazyListState = contentScrollState)
    }
    val shouldBeVisible by remember(composeHostState.isVisible) {
        derivedStateOf {
            if (composeHostState.isVisible) {
                // The scroll to hide behaviour should only be allowed when a SSInfoBar is currently being shown.
                (directionalLazyListState?.scrollDirection == ScrollDirection.SettledAtTop || directionalLazyListState?.scrollDirection == ScrollDirection.SettleAfterUpScroll)
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
                    if (composeHostState.isInfinite.value) Modifier.swipeable { composeHostState.dismiss() } else Modifier
                )
                .then(
                    if (directionalLazyListState != null) Modifier
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
                    OfflineInfoBar(
                        offlineData = SSComposeInfoBarData(
                            title = stringResource(R.string.oops_seems_like_you_are_offline).toTextType(),
                            description = stringResource(R.string.kindly_check_your_network_connection).toTextType()
                        )
                    )
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
 * @param contentScrollState The LazyListState which wil be used to show and hide the [SSComposeInfoBar] on scrolling.
 * @param enableNetworkMonitoring The flag that will decide whether the network monitoring feature is enabled or not.
 * @param content content of the screen on which the [SSComposeInfoBar] will be shown.
 */
@Composable
fun SSComposeInfoHost(
    modifier: Modifier = Modifier,
    composeHostState: SSComposeInfoHostState,
    direction: SSComposeInfoBarDirection = Top,
    contentScrollState: LazyListState? = null,
    enableNetworkMonitoring: Boolean = false,
    content: @Composable () -> Unit
) {
    composeHostState.setDirection(direction)
    val exitAnimation = getExitAnimation(composeHostState.direction.value)
    val enterAnimation = getEnterAnimation(composeHostState.direction.value)

    // For network monitoring
    val context = LocalContext.current
    val monitor = remember { ConnectivityObserver(context) }
    var isOnline: State<Boolean>? = null
    if (enableNetworkMonitoring) {
        isOnline = monitor.isOnline.collectAsStateWithLifecycle(initialValue = true)
    }

    // For Scroll-to-hide feature
    var directionalLazyListState: DirectionalLazyListState? = null
    if (contentScrollState != null) {
        directionalLazyListState =
            rememberDirectionalLazyListState(lazyListState = contentScrollState)
    }
    val shouldBeVisible by remember(composeHostState.isVisible) {
        derivedStateOf {
            if (composeHostState.isVisible) {
                // The scroll to hide behaviour should only be allowed when a SSInfoBar is currently being shown.
                (directionalLazyListState?.scrollDirection == ScrollDirection.SettledAtTop || directionalLazyListState?.scrollDirection == ScrollDirection.SettleAfterUpScroll)
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
                    if (composeHostState.isInfinite.value) Modifier.swipeable { composeHostState.dismiss() } else Modifier
                )
                .then(
                    if (directionalLazyListState != null) Modifier
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
                    OfflineInfoBar(
                        offlineData = SSComposeInfoBarData(
                            title = stringResource(R.string.oops_seems_like_you_are_offline).toTextType(),
                            description = stringResource(R.string.kindly_check_your_network_connection).toTextType()
                        )
                    )
                }
            }
        }
    }
}