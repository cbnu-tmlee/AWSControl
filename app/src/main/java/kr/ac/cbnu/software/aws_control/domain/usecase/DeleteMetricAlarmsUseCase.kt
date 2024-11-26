package kr.ac.cbnu.software.aws_control.domain.usecase

import kr.ac.cbnu.software.aws_control.data.repository.MetricAlarmRepository
import javax.inject.Inject
import kotlin.io.encoding.ExperimentalEncodingApi

internal class DeleteMetricAlarmsUseCase @Inject constructor(
    private val metricAlarmRepository: MetricAlarmRepository
) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(
        alarmNames: List<String>,
    ) = metricAlarmRepository.deleteAlarms(
        alarmNames = alarmNames,
    )
}