package kr.ac.cbnu.software.aws_control.domain.usecase

import aws.sdk.kotlin.services.ec2.model.Instance
import kr.ac.cbnu.software.aws_control.data.repository.InstanceRepository
import javax.inject.Inject

internal class GetInstancesUseCase @Inject constructor(
    private val instanceRepository: InstanceRepository
) {
    suspend operator fun invoke(): List<Instance> {
        val reservations = instanceRepository.getInstances()
        val instances = mutableListOf<Instance>()

        reservations.forEach { reservation ->
            reservation.instances?.let {
                instances.addAll(it)
            }
        }

        return instances
    }
}