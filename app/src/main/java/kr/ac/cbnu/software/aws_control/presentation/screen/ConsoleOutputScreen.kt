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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.ConsoleOutputUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.ConsoleOutputViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun ConsoleOutputScreen(
    viewModel: ConsoleOutputViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    ConsoleOutputScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onInstanceSelect = viewModel::selectInstance,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConsoleOutputScreen(
    modifier: Modifier = Modifier,
    uiState: ConsoleOutputUiState,
    onBackClick: () -> Unit,
    onInstanceSelect: (Instance?) -> Unit,
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
                        text = stringResource(R.string.console_output)
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
            verticalArrangement = if (uiState is ConsoleOutputUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                ConsoleOutputUiState.Loading ->
                    CircularProgressIndicator()

                is ConsoleOutputUiState.Success -> {
                    InstanceList(
                        instances = uiState.instances,
                        onInstanceClick = onInstanceSelect,
                    )

                    if (uiState.selectedInstance != null && uiState.output != null)
                        ConsoleOutputBottomSheet(
                            selectedInstance = uiState.selectedInstance,
                            output = uiState.output,
                            onDismiss = { onInstanceSelect(null) },
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConsoleOutputBottomSheet(
    selectedInstance: Instance,
    output: String,
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
                selectedInstance.name,
                style = MaterialTheme.typography.titleLarge,
            )
            ConsoleOutputContent(
                output = output
            )
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

@Composable
private fun ConsoleOutputContent(
    modifier: Modifier = Modifier,
    output: String
) {
    val scrollState = rememberScrollState(0)

    LaunchedEffect(output) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Text(
        text = output,
        modifier = modifier
            .verticalScroll(scrollState)
            .background(Color.Black)
            .padding(16.dp),
        color = Color.White,
    )
}
