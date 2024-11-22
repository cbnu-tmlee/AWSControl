package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.Instance
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetConsoleScreenshotUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.GetInstancesUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class ConsoleScreenshotViewModel @Inject constructor(
    private val getInstancesUseCase: GetInstancesUseCase,
    private val getConsoleScreenshotUseCase: GetConsoleScreenshotUseCase,
) : ContainerHost<ConsoleScreenshotUiState, Nothing>, ViewModel() {
    override val container =
        container<ConsoleScreenshotUiState, Nothing>(initialState = ConsoleScreenshotUiState.Loading) {
            loadInstances()
        }

    fun selectInstance(instance: Instance?) = intent(registerIdling = false) {
        require(state is ConsoleScreenshotUiState.Success)

        val screenshot =
            if (instance != null) getConsoleScreenshotUseCase(instance.instanceId!!) else null

        reduce {
            val successState = state as ConsoleScreenshotUiState.Success
            successState.copy(
                selectedInstance = instance,
                screenshot = screenshot,
            )
        }
    }

    private fun loadInstances() = intent(registerIdling = false) {
        repeatOnSubscription {
            val instances: List<Instance> = getInstancesUseCase()

            reduce {
                ConsoleScreenshotUiState.Success(
                    instances = instances,
                )
            }
        }
    }
}

@Immutable
internal sealed interface ConsoleScreenshotUiState {
    data object Loading : ConsoleScreenshotUiState

    data class Success(
        val instances: List<Instance>,
        val selectedInstance: Instance? = null,
        val screenshot: ByteArray? = null
    ) : ConsoleScreenshotUiState
}
