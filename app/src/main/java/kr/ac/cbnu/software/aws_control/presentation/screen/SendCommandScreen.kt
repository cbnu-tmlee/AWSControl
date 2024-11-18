package kr.ac.cbnu.software.aws_control.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import kotlinx.coroutines.launch
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.InstanceItem
import kr.ac.cbnu.software.aws_control.presentation.screen.util.name
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.SendCommandUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.SendCommandViewModel
import org.orbitmvi.orbit.compose.collectAsState

internal enum class SendCommandScreenType(
    val command: String? = null,
    val autoSend: Boolean = false,
) {
    DEFAULT(),
    CONDOR_STATUS("condor_status", true),
    CONDOR_SUBMIT("condor_submit"),
    CONDOR_Q("condor_q", true)
}

@Composable
internal fun SendCommandScreen(
    screenType: SendCommandScreenType,
    viewModel: SendCommandViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    SendCommandScreen(
        screenType = screenType,
        uiState = uiState,
        onBackClick = onBackClick,
        onInstanceSelect = viewModel::selectInstance,
        onSendCommand = viewModel::sendCommand,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SendCommandScreen(
    modifier: Modifier = Modifier,
    screenType: SendCommandScreenType,
    uiState: SendCommandUiState,
    onBackClick: () -> Unit,
    onInstanceSelect: (Instance?) -> Unit,
    onSendCommand: (String) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(
                            when (screenType) {
                                SendCommandScreenType.DEFAULT -> R.string.send_command
                                SendCommandScreenType.CONDOR_STATUS -> R.string.condor_status
                                SendCommandScreenType.CONDOR_SUBMIT -> R.string.condor_submit
                                SendCommandScreenType.CONDOR_Q -> R.string.condor_q
                            }
                        )
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState is SendCommandUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                SendCommandUiState.Loading ->
                    CircularProgressIndicator()

                is SendCommandUiState.Success -> {
                    InstanceList(
                        instances = uiState.instances,
                        onInstanceClick = {
                            onInstanceSelect(it)

                            if (screenType.autoSend)
                                onSendCommand(screenType.command!!)
                        },
                    )

                    if (uiState.selectedInstance != null)
                        if (uiState.result != null)
                            CommandResultBottomSheet(
                                selectedInstance = uiState.selectedInstance,
                                status = uiState.result,
                                onDismiss = { onInstanceSelect(null) },
                            )
                        else if (screenType == SendCommandScreenType.DEFAULT)
                            CommandDialog(
                                onSendCommand = {
                                    onSendCommand(it)
                                    onInstanceSelect(null)
                                },
                                onDismiss = { onInstanceSelect(null) },
                            )
                        else if (screenType == SendCommandScreenType.CONDOR_SUBMIT)
                            JobFileDialog(
                                onSendCommand = {
                                    onSendCommand(it)
                                    onInstanceSelect(null)
                                },
                                onDismiss = { onInstanceSelect(null) },
                            )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstanceList(
    modifier: Modifier = Modifier,
    instances: List<Instance>,
    onInstanceClick: (Instance) -> Unit,
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollableState,
    ) {
        instances.forEach { instance ->
            item(key = instance.instanceId) {
                InstanceItem(
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.baseline_dns_24),
                            contentDescription = null,
                        )
                    },
                    name = instance.name,
                    instanceId = instance.instanceId,
                    imageId = instance.imageId,
                    instanceType = instance.instanceType,
                    instanceState = instance.state,
                    monitoringState = instance.monitoring?.state,
                    onClick = { onInstanceClick(instance) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommandResultBottomSheet(
    selectedInstance: Instance,
    status: String,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
            scope.launch {
                sheetState.hide()
            }
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = selectedInstance.name,
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            CommandResultContent(
                modifier = Modifier.fillMaxWidth(),
                status = status,
            )
        }
    }
}

@Composable
private fun CommandResultContent(
    modifier: Modifier = Modifier,
    status: String,
) {
    val scrollState = rememberScrollState(0)

    Text(
        text = status,
        modifier = modifier
            .verticalScroll(scrollState)
            .background(Color.Black)
            .padding(16.dp),
        color = Color.White,
    )
}

@Composable
private fun CommandDialog(
    onSendCommand: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var command by remember { mutableStateOf("") }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.input_command))
        },
        text = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = command,
                onValueChange = { command = it },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.baseline_terminal_24),
                        contentDescription = stringResource(R.string.command),
                    )
                },
                label = {
                    Text(text = stringResource(R.string.command))
                },
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSendCommand(command)
                }
            ) {
                Text(text = stringResource(R.string.send))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.close))
            }
        }
    )
}

@Composable
private fun JobFileDialog(
    onSendCommand: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var jobDescriptionFile by remember { mutableStateOf("") }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.input_job_description_file))
        },
        text = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = jobDescriptionFile,
                onValueChange = { jobDescriptionFile = it },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.baseline_insert_drive_file_24),
                        contentDescription = stringResource(R.string.job_description_file),
                    )
                },
                label = {
                    Text(text = stringResource(R.string.command))
                },
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSendCommand("sudo -u ec2-user condor_submit /home/ec2-user/$jobDescriptionFile")
                }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.close))
            }
        }
    )
}