package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.cloudwatch.model.Dimension
import kr.ac.cbnu.software.aws_control.data.repository.MetricAlarmRepository
import javax.inject.Inject
import kotlin.io.encoding.ExperimentalEncodingApi

internal class CreateMetricAlarmUseCase @Inject constructor(
    private val metricAlarmRepository: MetricAlarmRepository
) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(
        metricName: String,
        dimension: Dimension,
        name: String,
        description: String,
        threshold: Double
    ) = metricAlarmRepository.putMetricAlarm(
        metricName = metricName,
        dimension = dimension,
        name = name,
        description = description,
        threshold = threshold
    )
}