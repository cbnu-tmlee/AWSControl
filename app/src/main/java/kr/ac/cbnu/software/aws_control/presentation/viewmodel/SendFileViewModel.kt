package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.CreateFileUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.GetInstancesUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class SendFileViewModel @Inject constructor(
    private val getInstancesUseCase: GetInstancesUseCase,
    private val createFileUseCase: CreateFileUseCase,
) : ContainerHost<SendFileUiState, Nothing>, ViewModel() {
    override val container =
        container<SendFileUiState, Nothing>(initialState = SendFileUiState.Loading) {
            loadInstances()
        }

    fun sendFile(fileName: String, binary: ByteArray) = intent(registerIdling = false) {
        require(state is SendFileUiState.Success)
        val state = state as SendFileUiState.Success
        createFileUseCase(
            instanceIds = state.selectedInstances.map { it.instanceId!! },
            binary = binary,
            fileName = fileName,
        )
    }

    fun changeInstanceSelection(instance: Instance, selected: Boolean) =
        intent(registerIdling = false) {
            require(state is SendFileUiState.Success)

            reduce {
                val state = state as SendFileUiState.Success
                state.copy(
                    selectedInstances = if (selected)
                        state.selectedInstances + instance
                    else
                        state.selectedInstances - instance,
                )
            }
        }

    private fun loadInstances() = intent(registerIdling = false) {
        repeatOnSubscription {
            val instances: List<Instance> = getInstancesUseCase()

            reduce {
                SendFileUiState.Success(
                    instances = instances,
                )
            }
        }
    }
}

@Immutable
internal sealed interface SendFileUiState {
    data object Loading : SendFileUiState

    data class Success(
        val instances: List<Instance>,
        val selectedInstances: Set<Instance> = emptySet(),
    ) : SendFileUiState
}
