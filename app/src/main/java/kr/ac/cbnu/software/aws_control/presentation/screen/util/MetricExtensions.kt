package kr.ac.cbnu.software.aws_control.presentation.screen.util

import aws.sdk.kotlin.services.cloudwatch.model.Metric

val Metric.instanceId: String
    get() = dimensions?.find { it.name == "InstanceId" }?.value ?: ""