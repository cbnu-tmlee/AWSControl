package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetConsoleOutputUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.GetInstancesUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class ConsoleOutputViewModel @Inject constructor(
    private val getInstancesUseCase: GetInstancesUseCase,
    private val getConsoleOutputUseCase: GetConsoleOutputUseCase
) : ContainerHost<ConsoleOutputUiState, Nothing>, ViewModel() {
    override val container =
        container<ConsoleOutputUiState, Nothing>(initialState = ConsoleOutputUiState.Loading) {
            loadInstances()
        }

    fun selectInstance(instance: Instance?) = intent(registerIdling = false) {
        require(state is ConsoleOutputUiState.Success)

        val output: String? =
            if (instance != null) getConsoleOutputUseCase(instance.instanceId!!) else null

        reduce {
            val successState = state as ConsoleOutputUiState.Success
            successState.copy(
                selectedInstance = instance,
                output = output,
            )
        }
    }

    private fun loadInstances() = intent(registerIdling = false) {
        repeatOnSubscription {
            val instances: List<Instance> = getInstancesUseCase()

            reduce {
                ConsoleOutputUiState.Success(
                    instances = instances,
                )
            }
        }
    }
}

@Immutable
internal sealed interface ConsoleOutputUiState {
    data object Loading : ConsoleOutputUiState

    data class Success(
        val instances: List<Instance>,
        val selectedInstance: Instance? = null,
        val output: String? = null,
    ) : ConsoleOutputUiState
}
