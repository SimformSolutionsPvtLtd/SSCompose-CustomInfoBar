package com.simform.sscustominfobarapp.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.simform.sscustominfobar.defaultInfoBars.ErrorInfoBar
import com.simform.sscustominfobar.defaultInfoBars.SuccessInfoBar
import com.simform.sscustominfobar.defaultInfoBars.WarningInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarColors
import com.simform.sscustominfobar.main.SSComposeInfoBarData
import com.simform.sscustominfobar.main.SSComposeInfoDuration
import com.simform.sscustominfobar.main.SSComposeInfoHostState
import com.simform.sscustominfobar.main.toSSCustomBackground
import com.simform.sscustominfobar.utils.TextType
import com.simform.sscustominfobarapp.R
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
    Default,
    Error,
    Warning,
    Success,
    AnnotatedText,
    GradientDemoBrush,
    DrawableDemoSVG,
    DrawableDemoPNG,
    ActionInfoBar
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
        ButtonType.Default -> DummyDefaultInfoBar(
            title = content.title,
            description = content.description,
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )

        ButtonType.Error -> ErrorInfoBar(
            errorData = SSComposeInfoBarData(
                title = content.title,
                description = content.description,
            ),
            isInfinite = isInfinite,
            shape = shape,
            onCloseClicked = onClose
        )

        ButtonType.Warning -> WarningInfoBar(
            warningData = SSComposeInfoBarData(
                title = content.title,
                description = content.description,
            ),
            isInfinite = isInfinite,
            shape = shape,
            onCloseClicked = onClose
        )

        ButtonType.Success -> SuccessInfoBar(
            successData = SSComposeInfoBarData(
                title = content.title,
                description = content.description,
            ),
            isInfinite = isInfinite,
            shape = shape,
            onCloseClicked = onClose
        )

        ButtonType.AnnotatedText -> DummyDefaultInfoBar(
            title = content.title,
            description = content.description,
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )

        ButtonType.GradientDemoBrush -> DummyGradientBrushDemo(
            title = content.title,
            description = content.description,
            isInfinite = isInfinite,
            background = Brush.horizontalGradient(listOf(Color.Red, Color.Green, Color.Blue)),
            shape = shape,
            onClose = onClose
        )

        ButtonType.DrawableDemoSVG -> DummyInfobarWithSVGBackground(
            title = content.title,
            description = content.description,
            background = painterResource(id = R.drawable.liquid_cheese),
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )

        ButtonType.DrawableDemoPNG -> DummyInfoBarWithDrawableBackground(
            title = content.title,
            background = painterResource(id = R.drawable.wintery_sunburst),
            description = content.description,
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )

        ButtonType.ActionInfoBar -> DummyInfoBarWithAction(
            title = content.title,
            description = content.description,
            isInfinite = isInfinite,
            shape = shape,
            onClose = onClose
        )
    }
}

/**
 * Dummy InfoBar to show [SSComposeInfoBar] with action.
 *
 * @param title Title string for the [SSComposeInfoBar].
 * @param description Description string for the [SSComposeInfoBar].
 * @param isInfinite Flag that decides whether [SSComposeInfoBar]'s duration is infinite.
 * @param shape Shape of the [SSComposeInfoBar].
 * @param onClose Called when user clicks on the clear button on the [SSComposeInfoBar].
 */
@Composable
private fun DummyInfoBarWithAction(
    title: TextType,
    description: TextType? = null,
    isInfinite: Boolean,
    shape: Shape,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    SSComposeInfoBar(
        title = title,
        description = description,
        isInfinite = isInfinite,
        shape = shape,
        onCloseClicked = onClose,
        onActionClicked = {
            Toast.makeText(context, context.getString(R.string.action_clicked), Toast.LENGTH_SHORT)
                .show()
        }
    )
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
    description: TextType? = null,
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
 * Dummy [SSComposeInfoBar] with Drawable Background for demo purposes.
 *
 * @param title Title string for the [SSComposeInfoBar].
 * @param description Description string for the [SSComposeInfoBar].
 * @param background of type [Painter].
 * @param isInfinite Flag that decides whether [SSComposeInfoBar]'s duration is infinite.
 * @param shape Shape of the [SSComposeInfoBar].
 * @param onClose Called when user clicks on the clear button on the [SSComposeInfoBar].
 */
@Composable
private fun DummyInfoBarWithDrawableBackground(
    title: TextType,
    description: TextType? = null,
    background: Painter,
    isInfinite: Boolean,
    shape: Shape,
    onClose: () -> Unit
) {
    SSComposeInfoBar(
        title = title,
        description = description,
        customBackground = background.toSSCustomBackground(),
        contentColors = SSComposeInfoBarColors(
            iconColor = Color.Black,
            titleColor = Color.Black,
            descriptionColor = Color.Black,
            dismissIconColor = Color.Black
        ),
        shape = shape,
        isInfinite = isInfinite,
        onCloseClicked = onClose
    )
}

/**
 * Dummy [SSComposeInfoBar] with SVG Background for demo purposes.
 *
 * @param title Title string for the [SSComposeInfoBar].
 * @param background of type [Painter].
 * @param description Description string for the [SSComposeInfoBar].
 * @param isInfinite Flag that decides whether [SSComposeInfoBar]'s duration is infinite.
 * @param shape Shape of the [SSComposeInfoBar].
 * @param onClose Called when user clicks on the clear button on the [SSComposeInfoBar].
 */
@Composable
private fun DummyInfobarWithSVGBackground(
    title: TextType,
    description: TextType? = null,
    background: Painter,
    isInfinite: Boolean,
    shape: Shape,
    onClose: () -> Unit
) {
    SSComposeInfoBar(
        title = title,
        description = description,
        customBackground = background.toSSCustomBackground(),
        contentColors = SSComposeInfoBarColors(
            iconColor = Color.Black,
            titleColor = Color.Black,
            descriptionColor = Color.Black,
            dismissIconColor = Color.Black
        ),
        shape = shape,
        isInfinite = isInfinite,
        onCloseClicked = onClose
    )
}

/**
 * Dummy [SSComposeInfoBar] with Gradient Background Using Brush for demo purposes.
 *
 * @param title Title string for the [SSComposeInfoBar].
 * @param description Description string for the [SSComposeInfoBar].
 * @param background Background of type [Brush] to be applied to [SSComposeInfoBar].
 * @param isInfinite Flag that decides whether [SSComposeInfoBar]'s duration is infinite.
 * @param shape Shape of the [SSComposeInfoBar].
 * @param onClose Called when user clicks on the clear button on the [SSComposeInfoBar].
 */
@Composable
private fun DummyGradientBrushDemo(
    title: TextType,
    description: TextType? = null,
    background: Brush,
    isInfinite: Boolean,
    shape: Shape,
    onClose: () -> Unit
) {
    SSComposeInfoBar(
        title = title,
        description = description,
        customBackground = background.toSSCustomBackground(),
        contentColors = SSComposeInfoBarColors(
            iconColor = Color.White,
            titleColor = Color.White,
            descriptionColor = Color.White,
            dismissIconColor = Color.White
        ),
        shape = shape,
        isInfinite = isInfinite,
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
    description: TextType? = null,
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