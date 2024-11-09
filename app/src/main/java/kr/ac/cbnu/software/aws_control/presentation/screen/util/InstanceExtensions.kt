package kr.ac.cbnu.software.aws_control.presentation.screen.util

import aws.sdk.kotlin.services.ec2.model.Instance

val Instance.name: String
    get() = tags?.find { it.key == "Name" }?.value ?: ""