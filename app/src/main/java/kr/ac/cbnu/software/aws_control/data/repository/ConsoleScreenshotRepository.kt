package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.GetConsoleScreenshotRequest
import aws.sdk.kotlin.services.ec2.model.GetConsoleScreenshotRequest.Companion.invoke
import javax.inject.Inject

internal class ConsoleScreenshotRepository @Inject constructor(
    private val ec2Client: Ec2Client
) {
    suspend fun getConsoleScreenshot(instanceId: String): String? {
        val request = GetConsoleScreenshotRequest {
            this.instanceId = instanceId
        }

        val response = ec2Client.getConsoleScreenshot(request)
        return response.imageData
    }
}