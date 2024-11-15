package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.ec2.model.Image
import kr.ac.cbnu.software.aws_control.data.repository.ImageRepository
import javax.inject.Inject

internal class GetImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(): List<Image> =
        imageRepository.getImages(
            nameFilter = listOf("htcondor-slave-image")
        )
}