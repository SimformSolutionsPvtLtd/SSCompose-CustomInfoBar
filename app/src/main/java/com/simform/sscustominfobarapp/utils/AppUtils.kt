package com.simform.sscustominfobarapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarData
import com.simform.sscustominfobar.main.SSComposeInfoDuration
import com.simform.sscustominfobar.main.SSComposeInfoHostState
import com.simform.sscustominfobar.utils.TextType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Button type of the buttons displayed on the home screen.
 */
enum class ButtonType {
    DefaultSlideInFromTop,
    DefaultSlideInFromBottom,
    AnnotatedText
}

/**
 * Function to create [SSComposeInfoBar] based on the pressed button's type.
 *
 * @param type [ButtonType] of the button pressed.
 * @param content [SSComposeInfoBarData] which contains title and message for [SSComposeInfoBar].
 * @param isInfinite Flag that represents whether the [SSComposeInfoBar]'s Duration is infinite or not.
 * @param shape Shape of the [SSComposeInfoBar].
 * @param onClose Called when user clicks on clear button on [SSComposeInfoBar] (Note: This Clear button will only be visible if isInfinite is true).
 */
@Composable
fun InfoBarByButtonType(
    type: ButtonType,
    content: SSComposeInfoBarData,
    isInfinite: Boolean,
    shape: Shape,
    onClose: () -> Unit = {}
) {
    return when (type) {
        ButtonType.DefaultSlideInFromTop -> DummyDefaultInfoBar(
            title = content.title,
            description = content.description,
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )

        ButtonType.DefaultSlideInFromBottom -> DummyDefaultInfoBar(
            title = content.title,
            description = content.description,
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )

        ButtonType.AnnotatedText -> DummyDefaultInfoBar(
            title = content.title,
            description = content.description,
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )
    }
}

/**
 * Dummy Default [SSComposeInfoBar] for the demo purposes.
 *
 * @param title Title string for the [SSComposeInfoBar].
 * @param description Description string for the [SSComposeInfoBar].
 * @param isInfinite Flag that decides whether [SSComposeInfoBar]'s duration is infinite.
 * @param shape Shape of the [SSComposeInfoBar].
 * @param onClose Called when user clicks on the clear button on the [SSComposeInfoBar].
 */
@Composable
private fun DummyDefaultInfoBar(
    title: TextType,
    description: TextType,
    isInfinite: Boolean,
    shape: Shape,
    onClose: () -> Unit
) {
    SSComposeInfoBar(
        title = title,
        description = description,
        isInfinite = isInfinite,
        shape = shape,
        onCloseClicked = onClose
    )
}

/**
 * Extension function on [CoroutineScope] to show [SSComposeInfoBar].
 *
 * @param title title of the [SSComposeInfoBar].
 * @param description description of the [SSComposeInfoBar].
 * @param composeInfoHostState [SSComposeInfoHostState] used to show the [SSComposeInfoBar].
 * @param duration duration for the [SSComposeInfoBar].
 * @param context additional to CoroutineScope.coroutineContext context of the coroutine.
 * @param start coroutine start option. The default value is CoroutineStart.DEFAULT.
 *
 * @return [Job] to control the coroutine.
 */
fun CoroutineScope.showSSComposeInfoBar(
    title: TextType,
    description: TextType,
    composeInfoHostState: SSComposeInfoHostState,
    duration: SSComposeInfoDuration,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT
) = launch(context, start) {
    composeInfoHostState.show(
        infoBarData = SSComposeInfoBarData(title, description),
        duration = duration
    )
}