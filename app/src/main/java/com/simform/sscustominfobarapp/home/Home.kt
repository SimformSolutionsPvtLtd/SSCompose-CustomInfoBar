package com.simform.sscustominfobarapp.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection
import com.simform.sscustominfobar.main.SSComposeInfoBarShapes
import com.simform.sscustominfobar.main.SSComposeInfoDuration
import com.simform.sscustominfobar.main.SSComposeInfoHost
import com.simform.sscustominfobar.main.SSComposeInfoHostState
import com.simform.sscustominfobarapp.R
import com.simform.sscustominfobarapp.utils.AppDimens
import com.simform.sscustominfobarapp.utils.ButtonType
import com.simform.sscustominfobarapp.utils.InfoBarByButtonType
import com.simform.sscustominfobarapp.utils.getButtonTitle
import com.simform.sscustominfobarapp.utils.getInfoBarDescription
import com.simform.sscustominfobarapp.utils.getInfoBarTitle
import com.simform.sscustominfobarapp.utils.showSSComposeInfoBar
import java.util.LinkedList
import java.util.Queue

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
        shape = MaterialTheme.shapes.small,
        onClick = {
            onClick(buttonType)
        }) {
        Text(text = title)
    }
}

/**
 * Custom Action bar to display app title and a setting icon.
 *
 * @param modifier The [Modifier] that is applied to this Custom action bar.
 * @param onSettingClicked Called when the user clicks on the settings icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActionBar(modifier: Modifier = Modifier, onSettingClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.ss_compose_info_bar_demo),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        modifier = modifier,
        actions = {
            IconButton(onClick = onSettingClicked) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.info_bar_settings),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

/**
 * Home Screen for the demonstration of the [SSComposeInfoBar] Library.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SSCustomInfoBarHome() {
    val context = LocalContext.current
    val composeInfoHostState by remember {
        mutableStateOf(SSComposeInfoHostState())
    }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var buttonType by remember {
        mutableStateOf(ButtonType.Default)
    }
    var shouldShowSettingSheet by remember {
        mutableStateOf(false)
    }
    var duration by remember {
        mutableStateOf(SSComposeInfoDuration.Short)
    }
    var direction by remember {
        mutableStateOf((SSComposeInfoBarDirection.Top))
    }
    val btnTypeQueue: Queue<ButtonType> = remember {
        LinkedList()
    }
    var isSwipeToDismissEnabled by remember {
        mutableStateOf(false)
    }
    var isNetworkMonitoringEnabled by remember {
        mutableStateOf(true)
    }
    composeInfoHostState.setOnInfoBarDismiss {
        btnTypeQueue.remove()
        if (btnTypeQueue.isNotEmpty()) {
            buttonType = btnTypeQueue.element()
        }
        Toast.makeText(
            context,
            context.getString(R.string.info_bar_dismissed_successfully),
            Toast.LENGTH_SHORT
        ).show()
    }
    val sheetState = rememberModalBottomSheetState(true)
    Scaffold(
        modifier = Modifier,
        topBar = {
            HomeActionBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                shouldShowSettingSheet = shouldShowSettingSheet.not()
            }
        }
    ) {
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            composeHostState = composeInfoHostState,
            direction = direction,
            contentScrollState = lazyListState,
            enableNetworkMonitoring = isNetworkMonitoringEnabled,
            isSwipeToDismissEnabled = isSwipeToDismissEnabled,
            composeInfoBar = { content ->
                InfoBarByButtonType(
                    type = buttonType,
                    content = content,
                    isInfinite = composeInfoHostState.isInfinite.value,
                    shape = if (composeInfoHostState.direction.value == SSComposeInfoBarDirection.Top) SSComposeInfoBarShapes.roundedBottom else SSComposeInfoBarShapes.roundedTop,
                    onClose = {
                        composeInfoHostState.dismiss()
                    }
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimens.DpMedium),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppDimens.DpMedium)
            ) {
                items(ButtonType.entries) { bType ->
                    CustomHomeButton(
                        title = stringResource(id = getButtonTitle(bType)),
                        buttonType = bType
                    ) { btnType ->
                        val title = getInfoBarTitle(context, btnType)
                        val desc = getInfoBarDescription(context, btnType)

                        // This if and else logic is only required because of demo's purpose and not related to library.
                        if (btnTypeQueue.isNotEmpty()) {
                            btnTypeQueue.add(btnType)
                        } else {
                            buttonType = btnType
                            btnTypeQueue.add(btnType)
                        }
                        coroutineScope.showSSComposeInfoBar(
                            title = title,
                            description = desc,
                            composeInfoHostState = composeInfoHostState,
                            duration = duration
                        )
                    }
                }
                // For temporary usage will be removed in future.
                // add these invisible boxes to make the current list scrollable
                // in order to demonstrate scroll to show and hide feature
                if (composeInfoHostState.isVisible) {
                    items(20) {
                        Box(modifier = Modifier.height(24.dp))
                    }
                }
            }
            if (shouldShowSettingSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        shouldShowSettingSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    SettingBottomSheet(
                        inputDuration = duration,
                        inputDirection = direction,
                        inputSwipeToDismissState = isSwipeToDismissEnabled,
                        inputNetworkObserverState = isNetworkMonitoringEnabled,
                        onCancel = { shouldShowSettingSheet = false },
                        onConfirm = { selectedDuration, selectedDirection, swipeToDismissState, networkObserverState ->
                            duration = selectedDuration
                            direction = selectedDirection
                            isSwipeToDismissEnabled = swipeToDismissState
                            isNetworkMonitoringEnabled = networkObserverState
                            shouldShowSettingSheet = false
                        }
                    )
                }
            }
        }
    }
}