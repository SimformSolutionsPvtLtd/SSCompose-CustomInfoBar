package com.simform.sscustominfobarapp.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection
import com.simform.sscustominfobar.main.SSComposeInfoBarShapes
import com.simform.sscustominfobar.main.SSComposeInfoDuration
import com.simform.sscustominfobar.main.SSComposeInfoHost
import com.simform.sscustominfobar.main.SSComposeInfoHostState
import com.simform.sscustominfobar.utils.toTextType
import com.simform.sscustominfobarapp.R
import com.simform.sscustominfobarapp.utils.AppDimens
import com.simform.sscustominfobarapp.utils.ButtonType
import com.simform.sscustominfobarapp.utils.InfoBarByButtonType
import com.simform.sscustominfobarapp.utils.showSSComposeInfoBar

/**
 * Custom Button used in home screen of the demo.
 *
 * @param title Text in the button.
 * @param buttonType [ButtonType] of the button.
 * @param onClick Called when user clicks on this button.
 */
@Composable
private fun CustomHomeButton(
    title: String,
    buttonType: ButtonType,
    onClick: (ButtonType) -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick(buttonType)
        }) {
        Text(text = title)
    }
}

/**
 * Custom Action bar created using row to display app title and a setting icon.
 *
 * @param modifier The [Modifier] that is applied to this Custom action bar.
 * @param onSettingClicked Called when the user clicks on the settings icon.
 */
@Composable
fun HomeActionBar(modifier: Modifier = Modifier, onSettingClicked: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.ss_compose_info_bar_demo))
        IconButton(onClick = onSettingClicked) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.info_bar_settings)
            )
        }
    }
}

/**
 * Home Screen for the demonstration of the [SSComposeInfoBar] Library.
 */
@Composable
fun SSCustomInfoBarHome() {
    val context = LocalContext.current
    val composeInfoHostState by remember {
        mutableStateOf(SSComposeInfoHostState())
    }
    val coroutineScope = rememberCoroutineScope()
    var buttonType by remember {
        mutableStateOf(ButtonType.Default)
    }
    var shouldShowSettingDialog by remember {
        mutableStateOf(false)
    }
    var duration by remember {
        mutableStateOf(SSComposeInfoDuration.Short)
    }
    var direction by remember {
        mutableStateOf((SSComposeInfoBarDirection.Top))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize(),
            composeHostState = composeInfoHostState,
            direction = direction,
            composeInfoBar = { content ->
                InfoBarByButtonType(
                    type = buttonType,
                    content = content,
                    isInfinite = composeInfoHostState.isInfinite.value,
                    shape = if (composeInfoHostState.direction.value == SSComposeInfoBarDirection.Top) SSComposeInfoBarShapes.roundedBottom else SSComposeInfoBarShapes.roundedTop,
                    onClose = {
                        composeInfoHostState.hide()
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimens.DpMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppDimens.DpMedium)
            ) {
                HomeActionBar(modifier = Modifier.fillMaxWidth()) {
                    shouldShowSettingDialog = shouldShowSettingDialog.not()
                }
                CustomHomeButton(
                    title = stringResource(id = R.string.show_default_info_bar),
                    buttonType = ButtonType.Default
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            context.getString(R.string.hey_user_good_morning).toTextType(),
                            context.getString(R.string.we_hope_you_are_doing_well_in_your_life)
                                .toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                CustomHomeButton(
                    title = stringResource(R.string.show_warning_info_bar),
                    buttonType = ButtonType.Warning
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            context.getString(R.string.warning).toTextType(),
                            context.getString(R.string.trying_to_access_sensitive_information)
                                .toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                CustomHomeButton(
                    title = stringResource(id = R.string.show_error_info_bar),
                    buttonType = ButtonType.Error
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            context.getString(R.string.error).toTextType(),
                            context.getString(R.string.failed_to_fetch_data_from_the_server)
                                .toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                CustomHomeButton(
                    title = stringResource(id = R.string.show_success_info_bar),
                    buttonType = ButtonType.Success
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            context.getString(R.string.success).toTextType(),
                            context.getString(R.string.successfully_fetched_network_data)
                                .toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                val titleAnnotated = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Red)) {
                        append(stringResource(R.string.hello))
                    }
                    withStyle(SpanStyle(color = Color.Blue)) {
                        append(stringResource(R.string.world))
                    }
                }
                val descAnnotated = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Blue, fontSize = AppDimens.SpMedium)) {
                        append(stringResource(R.string.i))
                    }
                    withStyle(SpanStyle(color = Color.Red, fontSize = AppDimens.SpMedium)) {
                        append(stringResource(R.string.love))
                    }
                    withStyle(SpanStyle(color = Color.White, fontSize = AppDimens.SpMedium)) {
                        append(stringResource(R.string.ss_compose_info_bar))
                    }
                }
                CustomHomeButton(
                    title = context.getString(R.string.show_annotated_text_info_bar),
                    buttonType = ButtonType.AnnotatedText
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            titleAnnotated.toTextType(),
                            descAnnotated.toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                CustomHomeButton(
                    title = stringResource(R.string.gradient_demo_using_brush),
                    buttonType = ButtonType.GradientDemoBrush
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            context.getString(R.string.success).toTextType(),
                            context.getString(R.string.successfully_fetched_network_data)
                                .toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                CustomHomeButton(
                    title = stringResource(R.string.drawable_demo_using_svg),
                    buttonType = ButtonType.DrawableDemoSVG
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            context.getString(R.string.success).toTextType(),
                            context.getString(R.string.successfully_fetched_network_data)
                                .toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                CustomHomeButton(
                    title = stringResource(R.string.drawable_demo_using_png),
                    buttonType = ButtonType.DrawableDemoPNG
                ) { btnType ->
                    if (!composeInfoHostState.isVisible) {
                        buttonType = btnType
                        coroutineScope.showSSComposeInfoBar(
                            context.getString(R.string.success).toTextType(),
                            context.getString(R.string.successfully_fetched_network_data)
                                .toTextType(),
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = shouldShowSettingDialog) {
            SettingDialog(
                modifier = Modifier.align(Alignment.Center),
                inputDuration = duration,
                inputDirection = direction,
                onCancel = {
                    shouldShowSettingDialog = false
                },
                onConfirm = { selectedDuration, selectedDirection ->
                    duration = selectedDuration
                    direction = selectedDirection
                    shouldShowSettingDialog = false
                })
        }
    }
}