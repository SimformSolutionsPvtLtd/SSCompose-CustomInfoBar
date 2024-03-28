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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.simform.sscustominfobar.animation.SSAnimationType
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarData
import com.simform.sscustominfobar.main.SSComposeInfoDuration
import com.simform.sscustominfobar.main.SSComposeInfoHost
import com.simform.sscustominfobar.main.SSComposeInfoHostState
import com.simform.sscustominfobarapp.R
import com.simform.sscustominfobarapp.utils.AppDimens
import com.simform.sscustominfobarapp.utils.ButtonType
import com.simform.sscustominfobarapp.utils.InfoBarByButtonType
import kotlinx.coroutines.launch

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
        mutableStateOf(ButtonType.DefaultSlideInFromTop)
    }
    var shouldShowSettingDialog by remember {
        mutableStateOf(false)
    }
    var duration by remember {
        mutableStateOf(SSComposeInfoDuration.Short)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize(),
            composeHostState = composeInfoHostState,
            composeInfoBar = { infoBarConfig ->
                InfoBarByButtonType(
                    type = buttonType,
                    composeHostState = composeInfoHostState,
                    composeInfoConfig = infoBarConfig
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
                    title = context.getString(R.string.slide_in_from_top),
                    buttonType = ButtonType.DefaultSlideInFromTop
                ) { btnType ->
                    buttonType = btnType
                    coroutineScope.launch {
                        composeInfoHostState.show(
                            infoBarData = SSComposeInfoBarData(
                                context.getString(R.string.slide_in_from_top),
                                context.getString(R.string.animated_from_top)
                            ),
                            duration = duration,
                            animationType = SSAnimationType.SlideInFromTop
                        )
                    }
                }
                CustomHomeButton(
                    title = context.getString(R.string.slide_in_from_bottom),
                    buttonType = ButtonType.DefaultSlideInFromBottom
                ) { btnType ->
                    buttonType = btnType
                    coroutineScope.launch {
                        composeInfoHostState.show(
                            infoBarData = SSComposeInfoBarData(
                                context.getString(R.string.slide_in_from_bottom),
                                context.getString(R.string.animated_from_bottom)
                            ),
                            duration = duration,
                            animationType = SSAnimationType.SlideInFromBottom
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = shouldShowSettingDialog) {
            SettingDialog(
                modifier = Modifier.align(Alignment.Center),
                selectedDuration = duration,
                onCancel = {
                    shouldShowSettingDialog = false
                },
                onConfirm = { selectedDuration ->
                    duration = selectedDuration
                    shouldShowSettingDialog = false
                })
        }
    }

}