package kr.ac.cbnu.software.aws_control.domain.usecase

import kr.ac.cbnu.software.aws_control.data.repository.InstanceRepository
import javax.inject.Inject

internal class TerminateInstancesUseCase @Inject constructor(
    private val instanceRepository: InstanceRepository
) {
    suspend operator fun invoke(instanceIds: List<String>) =
        instanceRepository.rebootInstances(instanceIds)
}