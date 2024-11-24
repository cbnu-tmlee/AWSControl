package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.cloudwatch.model.Metric
import aws.sdk.kotlin.services.cloudwatch.model.MetricDataResult
import kr.ac.cbnu.software.aws_control.data.repository.MetricRepository
import javax.inject.Inject

internal class GetMetricDataUseCase @Inject constructor(
    private val metricRepository: MetricRepository
) {
    suspend operator fun invoke(metric: Metric): MetricDataResult? =
        metricRepository.getMetricData(metric)
}