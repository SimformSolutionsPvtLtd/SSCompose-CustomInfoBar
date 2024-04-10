package com.simform.sscustominfobarapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
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
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoDuration
import com.simform.sscustominfobarapp.R
import com.simform.sscustominfobarapp.utils.AppDimens

/**
 * Custom dialog to display settings that user can set for the [SSComposeInfoBar] Displayed.
 *
 * @param modifier
 * @param selectedDuration
 * @param onCancel
 * @param onConfirm
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDialog(
    modifier: Modifier = Modifier,
    selectedDuration: SSComposeInfoDuration,
    onCancel: () -> Unit,
    onConfirm: (SSComposeInfoDuration) -> Unit
) {
    var selectedOption by remember {
        mutableStateOf(selectedDuration)
    }
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = {}
    ) {
        Surface(Modifier.clip(MaterialTheme.shapes.medium)) {
            Column(Modifier.padding(AppDimens.DpMedium)) {
                Text(
                    text = stringResource(R.string.duration),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(AppDimens.DpExtraSmall))
                SSComposeInfoDuration.entries.forEach { duration ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = duration.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.DpMedium))
                        RadioButton(
                            selected = selectedOption == duration,
                            onClick = {
                                selectedOption = duration
                            })
                    }
                }
                Spacer(modifier = Modifier.height(AppDimens.DpMedium))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = onCancel) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(onClick = {
                        onConfirm(selectedOption)
                    }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}