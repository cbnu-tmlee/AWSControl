package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.cloudwatch.CloudWatchClient
import aws.sdk.kotlin.services.cloudwatch.model.ComparisonOperator
import aws.sdk.kotlin.services.cloudwatch.model.DeleteAlarmsRequest
import aws.sdk.kotlin.services.cloudwatch.model.DescribeAlarmsRequest
import aws.sdk.kotlin.services.cloudwatch.model.Dimension
import aws.sdk.kotlin.services.cloudwatch.model.MetricAlarm
import aws.sdk.kotlin.services.cloudwatch.model.PutMetricAlarmRequest
import aws.sdk.kotlin.services.cloudwatch.model.StandardUnit
import aws.sdk.kotlin.services.cloudwatch.model.Statistic
import javax.inject.Inject

internal class MetricAlarmRepository @Inject constructor(
    private val cloudWatchClient: CloudWatchClient
) {
    suspend fun putMetricAlarm(
        metricName: String,
        dimension: Dimension,
        name: String,
        description: String,
        threshold: Double
    ) {
        val request = PutMetricAlarmRequest {
            alarmName = name
            comparisonOperator = ComparisonOperator.GreaterThanThreshold
            evaluationPeriods = 1
            this.metricName = metricName
            namespace = "AWS/EC2"
            period = 60
            statistic = Statistic.fromValue("Average")
            this.threshold = threshold
            actionsEnabled = false
            alarmDescription = description
            unit = StandardUnit.fromValue("Seconds")
            dimensions = listOf(dimension)
        }

        cloudWatchClient.putMetricAlarm(request)
    }

    suspend fun describeAlarms(): List<MetricAlarm> {
        val request = DescribeAlarmsRequest {}

        val response = cloudWatchClient.describeAlarms(request)
        return response.metricAlarms ?: listOf()
    }

    suspend fun deleteAlarms(
        alarmNames: List<String>
    ) {
        val request = DeleteAlarmsRequest {
            this.alarmNames = alarmNames
        }

        cloudWatchClient.deleteAlarms(request)
    }
}