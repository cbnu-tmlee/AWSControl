package kr.ac.cbnu.software.aws_control.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import kotlinx.coroutines.launch
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.InstanceItem
import kr.ac.cbnu.software.aws_control.presentation.screen.util.name
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.ConsoleScreenshotUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.ConsoleScreenshotViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun ConsoleScreenshotScreen(
    viewModel: ConsoleScreenshotViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    ConsoleScreenshotScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onInstanceSelect = viewModel::selectInstance,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConsoleScreenshotScreen(
    modifier: Modifier = Modifier,
    uiState: ConsoleScreenshotUiState,
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
                        text = stringResource(R.string.instance_screenshot)
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
            verticalArrangement = if (uiState is ConsoleScreenshotUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                ConsoleScreenshotUiState.Loading ->
                    CircularProgressIndicator()

                is ConsoleScreenshotUiState.Success -> {
                    InstanceList(
                        instances = uiState.instances,
                        onInstanceClick = onInstanceSelect,
                    )

                    if (uiState.selectedInstance != null && uiState.screenshot != null)
                        ConsoleOutputBottomSheet(
                            selectedInstance = uiState.selectedInstance,
                            screenshot = uiState.screenshot,
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
    screenshot: ByteArray,
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
            ConsoleScreenshotContent(
                modifier = Modifier.fillMaxWidth(),
                screenshot = screenshot,
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
private fun ConsoleScreenshotContent(
    modifier: Modifier = Modifier,
    screenshot: ByteArray,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(screenshot)
            .build(),
        contentDescription = null,
    )
}