package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetInstancesUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.SendCommandUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class SendCommandViewModel @Inject constructor(
    private val getInstancesUseCase: GetInstancesUseCase,
    private val sendCommandUseCase: SendCommandUseCase,
) : ContainerHost<SendCommandUiState, Nothing>, ViewModel() {
    override val container =
        container<SendCommandUiState, Nothing>(initialState = SendCommandUiState.Loading) {
            loadInstances()
        }

    fun selectInstance(instance: Instance?) = intent(registerIdling = false) {
        require(state is SendCommandUiState.Success)

        reduce {
            val state = state as SendCommandUiState.Success
            state.copy(
                selectedInstance = instance,
                result = null,
            )
        }
    }

    fun sendCommand(command: String) = intent(registerIdling = false) {
        require(state is SendCommandUiState.Success)
        val state = state as SendCommandUiState.Success

        requireNotNull(state.selectedInstance)

        val result = sendCommandUseCase(
            instanceId = state.selectedInstance.instanceId!!,
            command = command,
        )

        reduce {
            state.copy(
                result = result,
            )
        }
    }

    private fun loadInstances() = intent(registerIdling = false) {
        repeatOnSubscription {
            val instances: List<Instance> = getInstancesUseCase()

            reduce {
                SendCommandUiState.Success(
                    instances = instances,
                )
            }
        }
    }
}

@Immutable
internal sealed interface SendCommandUiState {
    data object Loading : SendCommandUiState

    data class Success(
        val instances: List<Instance>,
        val selectedInstance: Instance? = null,
        val result: String? = null,
    ) : SendCommandUiState
}
