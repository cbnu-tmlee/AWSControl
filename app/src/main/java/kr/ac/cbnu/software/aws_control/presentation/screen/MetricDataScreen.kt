package kr.ac.cbnu.software.aws_control.presentation.screen

import android.text.Layout
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.cloudwatch.model.Metric
import aws.sdk.kotlin.services.cloudwatch.model.MetricDataResult
import aws.smithy.kotlin.runtime.time.epochMilliseconds
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLayeredComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shadow
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.dimensions
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.Corner
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.MetricItem
import kr.ac.cbnu.software.aws_control.presentation.screen.util.instanceId
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.MetricDataUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.MetricDataViewModel
import org.orbitmvi.orbit.compose.collectAsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun MetricDataScreen(
    viewModel: MetricDataViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    MetricDataScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onMetricSelect = viewModel::selectMetric,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetricDataScreen(
    modifier: Modifier = Modifier,
    uiState: MetricDataUiState,
    onBackClick: () -> Unit,
    onMetricSelect: (Metric?) -> Unit,
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
            verticalArrangement = if (uiState is MetricDataUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                MetricDataUiState.Loading ->
                    CircularProgressIndicator()

                is MetricDataUiState.Success -> {
                    MetricList(
                        metrics = uiState.metrics,
                        selectedMetric = uiState.selectedMetric,
                        onMetricClick = onMetricSelect,
                    )

                    if (uiState.selectedMetric != null && uiState.metricData != null)
                        MetricDataBottomSheet(
                            selectedMetric = uiState.selectedMetric,
                            metricData = uiState.metricData,
                            onDismiss = { onMetricSelect(null) },
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetricDataBottomSheet(
    selectedMetric: Metric,
    metricData: MetricDataResult,
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
                text = "${selectedMetric.instanceId} - ${selectedMetric.metricName}",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            MetricDataContent(
                modifier = Modifier.fillMaxWidth(),
                metricData = metricData,
            )
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
private fun MetricDataContent(
    modifier: Modifier = Modifier,
    metricData: MetricDataResult,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val marker = rememberMarker()

    LaunchedEffect(metricData.hashCode()) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {
                lineSeries {
                    series(
                        y = metricData.values ?: emptyList()
                    )
                }
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        remember { LineCartesianLayer.LineFill.single(fill(Color(0xffa485e0))) }
                    )
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = CartesianValueFormatter { _, x, _ ->
                    val timestamps = metricData.timestamps ?: emptyList()
                    val epoch = timestamps[x.toInt()].epochMilliseconds
                    DATETIME_FORMAT.format(Date(epoch))
                },
            ),
            marker = marker,
            layerPadding = cartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp),
            persistentMarkers = rememberExtraLambda(marker) { marker at PERSISTENT_MARKER_X },
        ),
        modelProducer = modelProducer,
        modifier = modifier,
        zoomState = rememberVicoZoomState(zoomEnabled = false),
    )
}

@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = markerCorneredShape(Corner.FullyRounded)
    val labelBackground = rememberShapeComponent(
        fill = fill(MaterialTheme.colorScheme.surfaceBright),
        shape = labelBackgroundShape,
        shadow = shadow(
            radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP.dp,
            dy = LABEL_BACKGROUND_SHADOW_DY_DP.dp
        ),
    )
    val label = rememberTextComponent(
        color = MaterialTheme.colorScheme.onSurface,
        textAlignment = Layout.Alignment.ALIGN_CENTER,
        padding = dimensions(8.dp, 4.dp),
        background = labelBackground,
        minWidth = TextComponent.MinWidth.fixed(40f),
    )
    val indicatorFrontComponent =
        rememberShapeComponent(fill(MaterialTheme.colorScheme.surface), CorneredShape.Pill)
    val indicatorCenterComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicatorRearComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicator = rememberLayeredComponent(
        rear = indicatorRearComponent,
        front = rememberLayeredComponent(
            rear = indicatorCenterComponent,
            front = indicatorFrontComponent,
            padding = dimensions(5.dp),
        ),
        padding = dimensions(10.dp),
    )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        object :
            DefaultCartesianMarker(
                label = label,
                labelPosition = labelPosition,
                indicator = if (showIndicator) {
                    { color ->
                        LayeredComponent(
                            rear = ShapeComponent(
                                Fill(ColorUtils.setAlphaComponent(color, 38)),
                                CorneredShape.Pill
                            ),
                            front = LayeredComponent(
                                rear = ShapeComponent(
                                    fill = Fill(color),
                                    shape = CorneredShape.Pill,
                                    shadow = Shadow(radiusDp = 12f, color = color),
                                ),
                                front = indicatorFrontComponent,
                                padding = dimensions(5.dp),
                            ),
                            padding = dimensions(10.dp),
                        )
                    }
                } else {
                    null
                },
                indicatorSizeDp = 36f,
                guideline = guideline,
            ) {
            override fun updateInsets(
                context: CartesianMeasuringContext,
                horizontalDimensions: HorizontalDimensions,
                model: CartesianChartModel,
                insets: Insets,
            ) {
                with(context) {
                    val baseShadowInsetDp =
                        CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP
                    var topInset = (baseShadowInsetDp - LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    var bottomInset = (baseShadowInsetDp + LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    when (labelPosition) {
                        LabelPosition.Top,
                        LabelPosition.AbovePoint -> topInset += label.getHeight(context) + tickSizeDp.pixels

                        LabelPosition.Bottom -> bottomInset += label.getHeight(context) + tickSizeDp.pixels
                        LabelPosition.AroundPoint -> {}
                    }
                    insets.ensureValuesAtLeast(top = topInset, bottom = bottomInset)
                }
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f
private const val PERSISTENT_MARKER_X = 7f

private val DATETIME_FORMAT = SimpleDateFormat("H:mm", Locale.KOREA)
