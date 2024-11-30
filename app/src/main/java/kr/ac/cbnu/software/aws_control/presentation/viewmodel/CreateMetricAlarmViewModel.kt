package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.cloudwatch.model.Metric
import aws.sdk.kotlin.services.cloudwatch.model.MetricDataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.CreateMetricAlarmUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.GetMetricDataUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.GetMetricsUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class CreateMetricAlarmViewModel @Inject constructor(
    private val getMetricsUseCase: GetMetricsUseCase,
    private val createMetricAlarmUseCase: CreateMetricAlarmUseCase,
) : ContainerHost<CreateMetricAlarmUiState, Nothing>, ViewModel() {
    override val container =
        container<CreateMetricAlarmUiState, Nothing>(initialState = CreateMetricAlarmUiState.Loading) {
            loadMetrics()
        }

    fun createMetricAlarm(
        metric: Metric,
        name: String,
        description: String,
        threshold: Double,
    ) = intent(registerIdling = false) {
        repeatOnSubscription {
            createMetricAlarmUseCase(
                metricName = metric.metricName!!,
                dimension = metric.dimensions!![0],
                name = name,
                description = description,
                threshold = threshold,
            )

            reduce {
                require(state is CreateMetricAlarmUiState.Success)
                val state = state as CreateMetricAlarmUiState.Success

                state.copy(
                    selectedMetric = null,
                )
            }
        }
    }

    fun selectMetric(metric: Metric?) = intent(registerIdling = false) {
        repeatOnSubscription {
            reduce {
                require(state is CreateMetricAlarmUiState.Success)
                val state = state as CreateMetricAlarmUiState.Success

                state.copy(
                    selectedMetric = metric,
                )
            }
        }
    }

    private fun loadMetrics() = intent(registerIdling = false) {
        repeatOnSubscription {
            val metrics: List<Metric> = getMetricsUseCase()

            reduce {
                CreateMetricAlarmUiState.Success(
                    metrics = metrics,
                )
            }
        }
    }
}

@Immutable
internal sealed interface CreateMetricAlarmUiState {
    data object Loading : CreateMetricAlarmUiState

    data class Success(
        val metrics: List<Metric>,
        val selectedMetric: Metric? = null,
    ) : CreateMetricAlarmUiState
}
