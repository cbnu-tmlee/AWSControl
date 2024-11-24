package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.cloudwatch.CloudWatchClient
import aws.sdk.kotlin.services.cloudwatch.model.GetMetricDataRequest
import aws.sdk.kotlin.services.cloudwatch.model.ListMetricsRequest
import aws.sdk.kotlin.services.cloudwatch.model.Metric
import aws.sdk.kotlin.services.cloudwatch.model.MetricDataQuery
import aws.sdk.kotlin.services.cloudwatch.model.MetricDataResult
import aws.sdk.kotlin.services.cloudwatch.model.MetricStat
import aws.smithy.kotlin.runtime.time.Instant
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

internal class MetricRepository @Inject constructor(
    private val cloudWatchClient: CloudWatchClient
) {
    suspend fun listMetrics(namespace: String): List<Metric> {
        val request = ListMetricsRequest {
            this.namespace = namespace
        }

        val response = cloudWatchClient.listMetrics(request)
        return response.metrics ?: emptyList()
    }

    suspend fun getMetricData(metric: Metric): MetricDataResult? {
        val request = GetMetricDataRequest {
            startTime = Instant.now().minus(1.days)
            endTime = Instant.now()
            metricDataQueries = listOf(
                MetricDataQuery {
                    metricStat = MetricStat {
                        stat = "Maximum"
                        period = 1
                        this.metric = metric
                    }
                    id = "metric_${System.currentTimeMillis()}"
                }
            )
        }

        val response = cloudWatchClient.getMetricData(request)
        return response.metricDataResults?.firstOrNull()
    }
}