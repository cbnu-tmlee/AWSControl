package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.ec2.model.Region
import kr.ac.cbnu.software.aws_control.data.repository.RegionRepository
import javax.inject.Inject

internal class GetRegionsUseCase @Inject constructor(
    private val regionRepository: RegionRepository
) {
    suspend operator fun invoke(): List<Region> = regionRepository.getRegions()
}