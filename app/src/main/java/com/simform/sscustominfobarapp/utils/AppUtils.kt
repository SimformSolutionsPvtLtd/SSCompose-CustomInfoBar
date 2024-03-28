package com.simform.sscustominfobarapp.utils

import androidx.compose.runtime.Composable
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoConfig
import com.simform.sscustominfobar.main.SSComposeInfoHostState

/**
 * Button type of the buttons displayed on the home screen.
 */
enum class ButtonType {
    DefaultSlideInFromTop,
    DefaultSlideInFromBottom
}

/**
 * Function to create [SSComposeInfoBar] based on the pressed button's type.
 *
 * @param type [ButtonType] of the button pressed.
 * @param composeHostState [SSComposeInfoHostState] for the SSComposeInfoHost.
 * @param composeInfoConfig [SSComposeInfoConfig] for the configuration of [SSComposeInfoBar].
 */
@Composable
fun InfoBarByButtonType(
    type: ButtonType,
    composeHostState: SSComposeInfoHostState,
    composeInfoConfig: SSComposeInfoConfig
) {
    return when (type) {
        ButtonType.DefaultSlideInFromTop -> DummyDefaultInfoBar(composeHostState, composeInfoConfig)
        ButtonType.DefaultSlideInFromBottom -> DummyDefaultInfoBar(
            composeHostState,
            composeInfoConfig
        )
    }
}

/**
 * Dummy Default [SSComposeInfoBar] for the demo purposes.
 *
 * @param composeHostState [SSComposeInfoHostState] for the SSComposeInfoHost.
 * @param composeInfoConfig [SSComposeInfoConfig] for the configuration of [SSComposeInfoBar].
 */
@Composable
private fun DummyDefaultInfoBar(
    composeHostState: SSComposeInfoHostState,
    composeInfoConfig: SSComposeInfoConfig
) {
    SSComposeInfoBar(
        title = composeHostState.currentComposeInfoBarData.value?.title ?: "",
        message = composeHostState.currentComposeInfoBarData.value?.message ?: "",
        shape = composeInfoConfig.shape,
        isInfinite = composeHostState.isInfinite.value,
        onCloseClicked = {
            composeHostState.hide()
        }
    )
}