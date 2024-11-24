package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.cloudwatch.model.Metric
import kr.ac.cbnu.software.aws_control.data.repository.MetricRepository
import javax.inject.Inject

internal class GetMetricsUseCase @Inject constructor(
    private val metricRepository: MetricRepository
) {
    suspend operator fun invoke(): List<Metric> =
        metricRepository.listMetrics(
            namespace = "AWS/EC2"
        )
}