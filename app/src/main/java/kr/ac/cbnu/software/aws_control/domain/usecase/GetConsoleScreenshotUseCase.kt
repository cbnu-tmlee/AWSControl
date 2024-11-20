package kr.ac.cbnu.software.aws_control.domain.usecase

import kr.ac.cbnu.software.aws_control.data.repository.ConsoleScreenshotRepository
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class GetConsoleScreenshotUseCase @Inject constructor(
    private val consoleScreenshotRepository: ConsoleScreenshotRepository
) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend operator fun invoke(instanceId: String): ByteArray? {
        val base64 = consoleScreenshotRepository.getConsoleScreenshot(instanceId)

        if (base64 == null)
            return null

        return Base64.Default.decode(base64)
    }
}