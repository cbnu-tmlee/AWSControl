package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aws.sdk.kotlin.services.ec2.model.Instance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ac.cbnu.software.aws_control.domain.usecase.GetInstancesUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.RebootInstancesUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.StartInstancesUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.StopInstancesUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.TerminateInstancesUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class InstanceListViewModel @Inject constructor(
    private val getInstancesUseCase: GetInstancesUseCase,
    private val startInstancesUseCase: StartInstancesUseCase,
    private val stopInstancesUseCase: StopInstancesUseCase,
    private val rebootInstancesUseCase: RebootInstancesUseCase,
    private val terminateInstancesUseCase: TerminateInstancesUseCase,
) : ContainerHost<InstanceListUiState, Nothing>, ViewModel() {
    override val container =
        container<InstanceListUiState, Nothing>(initialState = InstanceListUiState.Loading) {
            startRefresh()
        }

    private fun startRefresh() {
        viewModelScope.launch {
            while (true) {
                loadInstances()
                delay(1.seconds)
            }
        }
    }

    fun changeInstanceSelection(instance: Instance, selected: Boolean) =
        intent(registerIdling = false) {
            require(state is InstanceListUiState.Success)

            reduce {
                val state = state as InstanceListUiState.Success
                state.copy(
                    selectedInstances = if (selected)
                        state.selectedInstances + instance
                    else
                        state.selectedInstances - instance,
                )
            }
        }

    fun startInstances() = intent(registerIdling = false) {
        val instanceIds: List<String> = getSelectedInstanceIds(state)
        startInstancesUseCase(instanceIds)
    }

    fun stopInstances() = intent(registerIdling = false) {
        val instanceIds: List<String> = getSelectedInstanceIds(state)
        stopInstancesUseCase(instanceIds)
    }

    fun rebootInstances() = intent(registerIdling = false) {
        val instanceIds: List<String> = getSelectedInstanceIds(state)
        rebootInstancesUseCase(instanceIds)
    }

    fun terminateInstances() = intent(registerIdling = false) {
        val instanceIds: List<String> = getSelectedInstanceIds(state)
        terminateInstancesUseCase(instanceIds)
    }

    private fun getSelectedInstanceIds(state: InstanceListUiState): List<String> {
        assert(state is InstanceListUiState.Success)
        val successState = state as InstanceListUiState.Success

        return successState.selectedInstances
            .filter { it.instanceId != null }
            .map { it.instanceId!! }
    }

    private fun loadInstances() = intent(registerIdling = false) {
        repeatOnSubscription {
            val instances: List<Instance> = getInstancesUseCase()
            val successState = state as? InstanceListUiState.Success

            reduce {
                InstanceListUiState.Success(
                    instances = instances,
                    selectedInstances = successState?.selectedInstances ?: emptySet(),
                )
            }
        }
    }
}

@Immutable
internal sealed interface InstanceListUiState {
    data object Loading : InstanceListUiState

    data class Success(
        val instances: List<Instance> = emptyList(),
        val selectedInstances: Set<Instance> = emptySet(),
    ) : InstanceListUiState
}
