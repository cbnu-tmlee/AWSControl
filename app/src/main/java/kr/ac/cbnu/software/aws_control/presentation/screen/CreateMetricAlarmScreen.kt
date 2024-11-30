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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.cloudwatch.model.Metric
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.MetricItem
import kr.ac.cbnu.software.aws_control.presentation.screen.util.instanceId
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.CreateMetricAlarmUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.CreateMetricAlarmViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun CreateMetricAlarmScreen(
    viewModel: CreateMetricAlarmViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    CreateMetricAlarmScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onMetricSelect = viewModel::selectMetric,
        onCreateAlarm = viewModel::createMetricAlarm,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMetricAlarmScreen(
    modifier: Modifier = Modifier,
    uiState: CreateMetricAlarmUiState,
    onBackClick: () -> Unit,
    onMetricSelect: (Metric?) -> Unit,
    onCreateAlarm: (Metric, String, String, Double) -> Unit,
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
                        text = stringResource(R.string.metric_data)
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
            verticalArrangement = if (uiState is CreateMetricAlarmUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                CreateMetricAlarmUiState.Loading ->
                    CircularProgressIndicator()

                is CreateMetricAlarmUiState.Success -> {
                    MetricList(
                        metrics = uiState.metrics,
                        selectedMetric = uiState.selectedMetric,
                        onMetricClick = onMetricSelect,
                    )

                    if (uiState.selectedMetric != null)
                        MetricAlarmDialog(
                            metric = uiState.selectedMetric,
                            onCreateAlarm = { name, description, threshold ->
                                onCreateAlarm(
                                    uiState.selectedMetric,
                                    name,
                                    description,
                                    threshold,
                                )
                                onMetricSelect(null)
                            },
                            onDismiss = { onMetricSelect(null) },
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetricList(
    modifier: Modifier = Modifier,
    metrics: List<Metric>,
    selectedMetric: Metric?,
    onMetricClick: (Metric) -> Unit,
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollableState,
    ) {
        metrics.forEach { metric ->
            item(key = metric.hashCode()) {
                MetricItem(
                    selected = metric == selectedMetric,
                    instanceId = metric.instanceId,
                    metricName = metric.metricName ?: "",
                    onClick = { onMetricClick(metric) }
                )
            }
        }
    }
}


@Composable
private fun MetricAlarmDialog(
    metric: Metric,
    onCreateAlarm: (String, String, Double) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var threshold by remember { mutableStateOf(50.0) }

    AlertDialog(
        title = {
            Text(text = "${metric.metricName} - ${metric.dimensions!![0].name} ${stringResource(R.string.create_alarm)}")
        },
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = name,
                    onValueChange = { name = it },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.baseline_access_alarm_24),
                            contentDescription = stringResource(R.string.name),
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.name))
                    },
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = description,
                    onValueChange = { description = it },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.baseline_notes_24),
                            contentDescription = stringResource(R.string.description),
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.description))
                    },
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = threshold.toString(),
                    onValueChange = { threshold = it.toDouble() },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.baseline_percent_24),
                            contentDescription = "기준치",
                        )
                    },
                    label = {
                        Text(text = "기준치")
                    },
                )
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCreateAlarm(name, description, threshold)
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
