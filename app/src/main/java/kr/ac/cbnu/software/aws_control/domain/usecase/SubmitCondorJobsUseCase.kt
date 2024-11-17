package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.ssm.model.CommandInvocationStatus
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kr.ac.cbnu.software.aws_control.data.repository.CommandRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

internal class SubmitCondorJobsUseCase @Inject constructor(
    private val commandRepository: CommandRepository
) {
    suspend operator fun invoke(instanceId: String, fileName: String): String? = coroutineScope {
        val command = commandRepository.sendCommand(
            instanceIds = listOf(instanceId),
            commands = listOf("condor_submit \"$fileName\"")
        )

        if (command?.commandId == null)
            return@coroutineScope null

        delay(100.milliseconds)

        val commandId = command.commandId!!
        val response = commandRepository.getCommandInvocation(
            commandId = commandId,
            instanceId = instanceId,
        )

        return@coroutineScope if (response.status == CommandInvocationStatus.Success)
            response.standardOutputContent
        else
            response.standardErrorContent
    }
}