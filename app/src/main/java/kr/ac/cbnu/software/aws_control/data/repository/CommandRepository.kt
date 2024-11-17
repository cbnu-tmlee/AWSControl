package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.ssm.SsmClient
import aws.sdk.kotlin.services.ssm.model.Command
import aws.sdk.kotlin.services.ssm.model.CommandInvocationStatus
import aws.sdk.kotlin.services.ssm.model.GetCommandInvocationRequest
import aws.sdk.kotlin.services.ssm.model.GetCommandInvocationResponse
import aws.sdk.kotlin.services.ssm.model.SendCommandRequest
import aws.sdk.kotlin.services.ssm.model.SendCommandRequest.Companion.invoke
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.ac.cbnu.software.aws_control.data.di.Dispatcher
import kr.ac.cbnu.software.aws_control.data.di.DispatcherType
import javax.inject.Inject

internal class CommandRepository @Inject constructor(
    private val ssmClient: SsmClient,
    @Dispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun sendCommand(
        instanceIds: List<String>,
        commands: List<String>
    ): Command? {
        val request = SendCommandRequest {
            this.instanceIds = instanceIds
            documentName = "AWS-RunShellScript"
            parameters = mapOf(
                "commands" to commands
            )
        }

        val response = ssmClient.sendCommand(request)
        return response.command
    }

    suspend fun getCommandInvocation(
        commandId: String,
        instanceId: String,
    ): GetCommandInvocationResponse = withContext(ioDispatcher) {
        val request = GetCommandInvocationRequest {
            this.commandId = commandId
            this.instanceId = instanceId
        }

        var response: GetCommandInvocationResponse

        do {
            response = ssmClient.getCommandInvocation(request)
        } while (response.status in setOf(
                CommandInvocationStatus.Pending,
                CommandInvocationStatus.InProgress,
                CommandInvocationStatus.Delayed
            )
        )

        return@withContext response
    }
}