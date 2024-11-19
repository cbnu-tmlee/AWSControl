package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.GetConsoleOutputRequest
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class ConsoleOutputRepository @Inject constructor(
    private val ec2Client: Ec2Client
) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun getConsoleOutput(instanceId: String): String? {
        val request = GetConsoleOutputRequest {
            this.instanceId = instanceId
        }

        val response = ec2Client.getConsoleOutput(request)
        val base64 = response.output

        if (base64 == null)
            return null

        val bytes = Base64.Default.decode(base64)
        return String(bytes, charset("UTF-8"))
    }
}