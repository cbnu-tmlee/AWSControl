package kr.ac.cbnu.software.aws_control.domain.usecase

import kotlinx.coroutines.coroutineScope
import kr.ac.cbnu.software.aws_control.data.repository.CommandRepository
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class CreateFileUseCase @Inject constructor(
    private val commandRepository: CommandRepository
) {
    @OptIn(ExperimentalEncodingApi::class, ExperimentalUuidApi::class)
    suspend operator fun invoke(
        instanceIds: List<String>,
        binary: ByteArray,
        fileName: String
    ) = coroutineScope {
        val uuid = Uuid.random().toString()
        val base64 = Base64.Default.encode(binary)

        commandRepository.sendCommand(
            instanceIds = instanceIds,
            commands = listOfNotNull(
                "echo \"$base64\" > /tmp/${uuid}.txt",
                "base64 -d /tmp/${uuid}.txt > /home/ec2-user/$fileName",
                if (fileName.endsWith(".sh"))
                    "chmod +x /home/ec2-user/$fileName"
                else null
            )
        )
    }
}