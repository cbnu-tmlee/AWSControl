package kr.ac.cbnu.software.aws_control.domain.usecase

import kr.ac.cbnu.software.aws_control.data.repository.ConsoleOutputRepository
import javax.inject.Inject
import kotlin.io.encoding.ExperimentalEncodingApi

internal class GetConsoleOutputUseCase @Inject constructor(
    private val consoleOutputRepository: ConsoleOutputRepository
) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(instanceId: String): String? =
        consoleOutputRepository.getConsoleOutput(instanceId)
}