package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.ec2.model.AvailabilityZone
import kr.ac.cbnu.software.aws_control.data.repository.AvailabilityZoneRepository
import javax.inject.Inject

internal class GetAvailabilityZonesUseCase @Inject constructor(
    private val availabilityZoneRepository: AvailabilityZoneRepository
) {
    suspend operator fun invoke(): List<AvailabilityZone> =
        availabilityZoneRepository.getAvailabilityZones()
}