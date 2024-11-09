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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.InstanceItem
import kr.ac.cbnu.software.aws_control.presentation.screen.util.name
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.InstanceListUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.InstanceListViewModel
import org.orbitmvi.orbit.compose.collectAsState

internal enum class InstanceListScreenType {
    LIST,
    START,
    STOP,
    CREATE,
    REBOOT,
    TERMINATE,
}

@Composable
internal fun InstanceListScreen(
    screenType: InstanceListScreenType,
    viewModel: InstanceListViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    InstanceListScreen(
        uiState = uiState,
        screenType = screenType,
        onBackClick = onBackClick,
        onStartClick = viewModel::startInstances,
        onStopClick = viewModel::stopInstances,
        onRebootClick = viewModel::rebootInstances,
        onTerminateClick = viewModel::terminateInstances,
        onSelectionChange = viewModel::changeInstanceSelection,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstanceListScreen(
    modifier: Modifier = Modifier,
    screenType: InstanceListScreenType,
    uiState: InstanceListUiState,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onRebootClick: () -> Unit,
    onTerminateClick: () -> Unit,
    onSelectionChange: (Instance, Boolean) -> Unit,
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
                                InstanceListScreenType.LIST -> R.string.list_instances
                                InstanceListScreenType.START -> R.string.start_instances
                                InstanceListScreenType.STOP -> R.string.stop_instances
                                InstanceListScreenType.CREATE -> R.string.create_instance
                                InstanceListScreenType.REBOOT -> R.string.reboot_instance
                                InstanceListScreenType.TERMINATE -> R.string.terminate_instances
                            }
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            if (uiState is InstanceListUiState.Success)
                when (screenType) {
                    InstanceListScreenType.START ->
                        if (uiState.selectedInstances.isNotEmpty())
                            ExtendedFloatingActionButton(
                                onClick = onStartClick,
                                icon = {
                                    Icon(
                                        Icons.Filled.PlayArrow,
                                        contentDescription = stringResource(R.string.start),
                                    )
                                },
                                text = {
                                    Text(text = stringResource(R.string.start))
                                }
                            )

                    InstanceListScreenType.STOP ->
                        if (uiState.selectedInstances.isNotEmpty())
                            ExtendedFloatingActionButton(
                                onClick = onStopClick,
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_stop_24),
                                        contentDescription = stringResource(R.string.stop),
                                    )
                                },
                                text = {
                                    Text(text = stringResource(R.string.stop))
                                }
                            )

                    InstanceListScreenType.REBOOT ->
                        if (uiState.selectedInstances.isNotEmpty())
                            ExtendedFloatingActionButton(
                                onClick = onRebootClick,
                                icon = {
                                    Icon(
                                        Icons.Filled.Refresh,
                                        contentDescription = stringResource(R.string.reboot),
                                    )
                                },
                                text = {
                                    Text(text = stringResource(R.string.reboot))
                                }
                            )

                    InstanceListScreenType.TERMINATE ->
                        if (uiState.selectedInstances.isNotEmpty())
                            ExtendedFloatingActionButton(
                                onClick = onTerminateClick,
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_power_settings_new_24),
                                        contentDescription = stringResource(R.string.terminate),
                                    )
                                },
                                text = {
                                    Text(text = stringResource(R.string.terminate))
                                }
                            )

                    else -> {}
                }
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState is InstanceListUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                InstanceListUiState.Loading ->
                    CircularProgressIndicator()

                is InstanceListUiState.Success ->
                    InstanceList(
                        instances = uiState.instances,
                        screenType = screenType,
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
    screenType: InstanceListScreenType,
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
                val stateName = instance.state?.name?.value
                val enabled = if (stateName != "terminated") when (screenType) {
                    InstanceListScreenType.START -> stateName !in listOf(
                        "running",
                        "pending"
                    )

                    InstanceListScreenType.STOP -> stateName !in listOf(
                        "stopped",
                        "pending"
                    )

                    InstanceListScreenType.REBOOT -> stateName !in listOf(
                        "stopped",
                        "pending"
                    )

                    else -> true
                } else false

                InstanceItem(
                    leadingContent = {
                        if (screenType != InstanceListScreenType.LIST) {
                            Checkbox(
                                checked = selected,
                                enabled = enabled,
                                onCheckedChange = {
                                    onInstanceSelectionChange(instance, !selected)
                                },
                            )
                        } else
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
                    onClick = if (screenType != InstanceListScreenType.LIST && enabled) ({
                        onInstanceSelectionChange(instance, !selected)
                    } as (() -> Unit)?) else null,
                )
            }
        }
    }
}
