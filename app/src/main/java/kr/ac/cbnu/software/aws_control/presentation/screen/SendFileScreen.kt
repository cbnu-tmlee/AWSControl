package kr.ac.cbnu.software.aws_control.presentation.screen

import android.R.attr.onClick
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import coil3.util.CoilUtils.result
import kotlinx.coroutines.launch
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.InstanceItem
import kr.ac.cbnu.software.aws_control.presentation.screen.util.getFileBinary
import kr.ac.cbnu.software.aws_control.presentation.screen.util.getFileName
import kr.ac.cbnu.software.aws_control.presentation.screen.util.name
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.InstanceListUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.SendCommandUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.SendCommandViewModel
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.SendFileUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.SendFileViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun SendFileScreen(
    viewModel: SendFileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    SendFileScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSelectionChange = viewModel::changeInstanceSelection,
        onSendFile = viewModel::sendFile,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SendFileScreen(
    modifier: Modifier = Modifier,
    uiState: SendFileUiState,
    onBackClick: () -> Unit,
    onSelectionChange: (Instance, Boolean) -> Unit,
    onSendFile: (String, ByteArray) -> Unit,
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) {
            for (uri: Uri in it) {
                val fileName = getFileName(context, uri)
                val binary = getFileBinary(context, uri)

                Log.d("SendFileScreen", fileName)
                Log.d("SendFileScreen", binary.size.toString())
                onSendFile(fileName, binary)
            }
        }

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
                        text = stringResource(R.string.send_file)
                    )
                },
            )
        },
        floatingActionButton = {
            if (uiState is SendFileUiState.Success && uiState.selectedInstances.isNotEmpty())
                ExtendedFloatingActionButton(
                    onClick = {
                        launcher.launch(arrayOf("*/*"))
                    },
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.send),
                        )
                    },
                    text = {
                        Text(text = stringResource(R.string.send))
                    }
                )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState is SendFileUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                SendFileUiState.Loading ->
                    CircularProgressIndicator()

                is SendFileUiState.Success ->
                    InstanceList(
                        instances = uiState.instances,
                        selectedInstances = uiState.selectedInstances,
                        onInstanceSelectionChange = onSelectionChange,
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstanceList(
    modifier: Modifier = Modifier,
    instances: List<Instance>,
    selectedInstances: Set<Instance>,
    onInstanceSelectionChange: (Instance, Boolean) -> Unit,
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollableState,
    ) {
        instances.forEach { instance ->
            item(key = instance.instanceId) {
                val selected = selectedInstances.contains(instance)

                InstanceItem(
                    leadingContent = {
                        Checkbox(
                            checked = selected,
                            onCheckedChange = {
                                onInstanceSelectionChange(instance, !selected)
                            },
                        )
                    },
                    name = instance.name,
                    instanceId = instance.instanceId,
                    imageId = instance.imageId,
                    instanceType = instance.instanceType,
                    instanceState = instance.state,
                    monitoringState = instance.monitoring?.state,
                    onClick = {
                        onInstanceSelectionChange(instance, !selected)
                    }
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
            Text(text = "명령 입력")
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