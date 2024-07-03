package com.simform.sscustominfobarapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.simform.sscustominfobar.animation.AnimationType
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection
import com.simform.sscustominfobar.main.SSComposeInfoDuration
import com.simform.sscustominfobarapp.R
import com.simform.sscustominfobarapp.utils.AppDimens

/**
 * Custom Bottom Sheet to display settings that user can set for the [SSComposeInfoBar] Displayed.
 *
 * @param modifier The modifier to apply to [SettingBottomSheet].
 * @param inputDuration The default value for [SSComposeInfoDuration].
 * @param inputDirection The default value for [SSComposeInfoBarDirection].
 * @param inputSwipeToDismissState The default value for whether the swipe to dismiss is enabled or not.
 * @param inputNetworkObserverState The default value for whether the network observer is enabled or not.
 * @param onCancel Called when the user clicks on the cancel button.
 * @param onConfirm Called when the user clicks on the confirm button.
 */
@Composable
fun SettingBottomSheet(
    modifier: Modifier = Modifier,
    inputDuration: SSComposeInfoDuration,
    inputDirection: SSComposeInfoBarDirection,
    inputAnimationType: AnimationType,
    inputSwipeToDismissState: Boolean,
    inputNetworkObserverState: Boolean,
    onCancel: () -> Unit,
    onConfirm: (SSComposeInfoDuration, SSComposeInfoBarDirection, AnimationType, Boolean, Boolean) -> Unit
) {
    var selectedDuration by remember {
        mutableStateOf(inputDuration)
    }
    var selectedDirection by remember {
        mutableStateOf(inputDirection)
    }
    var selectedAnimationType by remember {
        mutableStateOf(inputAnimationType)
    }
    var isSwipeToDismissEnabled by remember {
        mutableStateOf(inputSwipeToDismissState)
    }
    var isNetworkObserverEnabled by remember {
        mutableStateOf(inputNetworkObserverState)
    }
    Surface(modifier.clip(MaterialTheme.shapes.medium)) {
        Column(
            Modifier
                .padding(AppDimens.DpMedium)
                .verticalScroll(rememberScrollState())
        ) {
            DurationSection(selectedOption = selectedDuration) { duration ->
                selectedDuration = duration
            }
            HorizontalDivider(modifier = Modifier.height(AppDimens.DpMedium))
            DirectionSection(selectedOption = selectedDirection) { direction ->
                selectedDirection = direction
            }
            HorizontalDivider(modifier = Modifier.height(AppDimens.DpMedium))
            AnimationSection(selectedOption = selectedAnimationType) { animationType ->
                selectedAnimationType = animationType
            }
            HorizontalDivider(modifier = Modifier.height(AppDimens.DpMedium))
            CustomChipSection(
                title = stringResource(R.string.swipe_to_dismiss),
                isSelected = isSwipeToDismissEnabled,
                onSelected = { isSwipeToDismissEnabled = it }
            )
            CustomChipSection(
                title = stringResource(R.string.network_state_observation),
                isSelected = isNetworkObserverEnabled,
                onSelected = { isNetworkObserverEnabled = it }
            )
            ButtonRow(
                onCancel = onCancel,
                onConfirm = {
                    onConfirm(
                        selectedDuration,
                        selectedDirection,
                        selectedAnimationType,
                        isSwipeToDismissEnabled,
                        isNetworkObserverEnabled
                    )
                }
            )
        }
    }
}


/**
 * Custom composable to show buttons in a row.
 * Here used for cancel and confirm buttons.
 *
 * @param onCancel Called when user clicks on cancel button
 * @param onConfirm Called when user clicks on confirm button
 */
@Composable
private fun ButtonRow(
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.cancel))
        }
        Spacer(modifier = Modifier.width(AppDimens.DpMedium))
        Button(
            onClick = onConfirm,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.confirm))
        }
    }
}

/**
 * [SettingBottomSheet]'s sub composable for selecting animationType of [SSComposeInfoBar].
 *
 * @param selectedOption currently selected option on [AnimationType].
 * @param onOptionSelected called when user selects a new animationType radio button.
 */
@Composable
private fun AnimationSection(
    selectedOption: AnimationType,
    onOptionSelected: (animationType: AnimationType) -> Unit
) {
    BaseSettingSection(
        sectionTitle = stringResource(id = R.string.animation),
        selectedOption = selectedOption.name,
        optionsList = AnimationType.entries.map { it.name }) { newAnimation ->
        onOptionSelected(AnimationType.valueOf(newAnimation))
    }
}

/**
 * [SettingBottomSheet]'s sub composable for selecting duration of [SSComposeInfoBar].
 *
 * @param selectedOption currently selected option on [SSComposeInfoDuration].
 * @param onOptionSelected called when user selects a new duration radio button.
 */
@Composable
private fun DurationSection(
    selectedOption: SSComposeInfoDuration,
    onOptionSelected: (selectedDuration: SSComposeInfoDuration) -> Unit
) {
    BaseSettingSection(
        sectionTitle = stringResource(id = R.string.duration),
        selectedOption = selectedOption.name,
        optionsList = SSComposeInfoDuration.entries.map { it.name }) { newSelectedDuration ->
        onOptionSelected(SSComposeInfoDuration.valueOf(newSelectedDuration))
    }
}

/**
 * [SettingBottomSheet]'s sub composable for selecting direction of [SSComposeInfoBar].
 *
 * @param selectedOption currently selected option on [SSComposeInfoBarDirection].
 * @param onOptionSelected called when user selects a new direction radio button.
 */
@Composable
private fun DirectionSection(
    selectedOption: SSComposeInfoBarDirection,
    onOptionSelected: (selectedDirection: SSComposeInfoBarDirection) -> Unit
) {
    BaseSettingSection(
        sectionTitle = stringResource(id = R.string.direction),
        selectedOption = selectedOption.name,
        optionsList = SSComposeInfoBarDirection.entries.map { it.name }) { newSelectedDirection ->
        onOptionSelected(SSComposeInfoBarDirection.valueOf(newSelectedDirection))
    }
}

/**
 * [SettingBottomSheet]'s sub composable for enabling/disabling a feature of [SSComposeInfoBar].
 *
 * @param title Name of the feature.
 * @param isSelected Initial info about whether it is selected or not.
 * @param onSelected Callback for when user changes the configuration.
 */
@Composable
private fun CustomChipSection(
    title: String,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
        )
        Spacer(modifier = Modifier.width(AppDimens.DpMedium))
        Switch(checked = isSelected, onCheckedChange = {
            onSelected(it)
        })
    }
}

/**
 * Base composable for settings radio buttons sections
 *
 * @param sectionTitle Title of the section.
 * @param selectedOption Currently selected option.
 * @param optionsList List of names of the available options.
 * @param onOptionSelected Called when user selects any one of the options.
 */
@Composable
private fun BaseSettingSection(
    sectionTitle: String,
    selectedOption: String,
    optionsList: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Text(
        text = sectionTitle,
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(AppDimens.DpExtraSmall))
    optionsList.forEach { option ->
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.width(AppDimens.DpMedium))
            RadioButton(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) }
            )
        }
    }
}