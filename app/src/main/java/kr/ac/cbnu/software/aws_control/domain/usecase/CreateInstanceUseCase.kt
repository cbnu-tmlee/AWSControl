package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.ec2.model.InstanceType
import kr.ac.cbnu.software.aws_control.data.repository.InstanceRepository
import javax.inject.Inject

internal class CreateInstanceUseCase @Inject constructor(
    private val instanceRepository: InstanceRepository
) {
    suspend operator fun invoke(
        name: String,
        imageId: String,
        instanceType: InstanceType
    ) =
        instanceRepository.runInstance(
            name = name,
            imageId = imageId,
            instanceType = instanceType,
        )
}