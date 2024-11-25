package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.cloudwatch.model.Metric
import aws.sdk.kotlin.services.cloudwatch.model.MetricDataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetMetricDataUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.GetMetricsUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class MetricDataViewModel @Inject constructor(
    private val getMetricsUseCase: GetMetricsUseCase,
    private val getMetricDataUseCase: GetMetricDataUseCase
) : ContainerHost<MetricDataUiState, Nothing>, ViewModel() {
    override val container =
        container<MetricDataUiState, Nothing>(initialState = MetricDataUiState.Loading) {
            loadMetrics()
        }

    fun selectMetric(metric: Metric?) = intent(registerIdling = false) {
        repeatOnSubscription {
            var metricData: MetricDataResult? =
                if (metric != null) getMetricDataUseCase(metric) else null

            reduce {
                require(state is MetricDataUiState.Success)
                val state = state as MetricDataUiState.Success

                state.copy(
                    selectedMetric = metric,
                    metricData = metricData,
                )
            }
        }
    }

    private fun loadMetrics() = intent(registerIdling = false) {
        repeatOnSubscription {
            val metrics: List<Metric> = getMetricsUseCase()

            reduce {
                MetricDataUiState.Success(
                    metrics = metrics,
                )
            }
        }
    }
}

@Immutable
internal sealed interface MetricDataUiState {
    data object Loading : MetricDataUiState

    data class Success(
        val metrics: List<Metric>,
        val selectedMetric: Metric? = null,
        val metricData: MetricDataResult? = null,
    ) : MetricDataUiState
}
