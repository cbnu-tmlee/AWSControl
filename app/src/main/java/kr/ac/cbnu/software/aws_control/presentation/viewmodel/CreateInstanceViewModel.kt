package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.Image
import aws.sdk.kotlin.services.ec2.model.InstanceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetImagesUseCase
import kr.ac.cbnu.software.aws_control.domain.usecase.CreateInstanceUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class CreateInstanceViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val createInstanceUseCase: CreateInstanceUseCase
) : ContainerHost<CreateInstanceUiState, Nothing>, ViewModel() {
    override val container =
        container<CreateInstanceUiState, Nothing>(CreateInstanceUiState.Loading) {
            loadImages()
        }

    private fun loadImages() = intent(registerIdling = false) {
        repeatOnSubscription {
            val images: List<Image> = getImagesUseCase()

            reduce {
                CreateInstanceUiState.Success(
                    images = images,
                    selectedImageId = images[0].imageId!!,
                )
            }
        }
    }

    fun setName(name: String) = intent(registerIdling = false) {
        val state = state as CreateInstanceUiState.Success

        reduce {
            state.copy(
                name = name,
            )
        }
    }

    fun selectImage(imageId: String) = intent(registerIdling = false) {
        val state = state as CreateInstanceUiState.Success

        reduce {
            state.copy(
                selectedImageId = imageId,
            )
        }
    }

    fun selectInstanceType(instanceType: InstanceType) = intent(registerIdling = false) {
        val state = state as CreateInstanceUiState.Success

        reduce {
            state.copy(
                selectedInstanceType = instanceType
            )
        }
    }

    fun runInstance() = intent(registerIdling = false) {
        require(state is CreateInstanceUiState.Success)

        val state = state as CreateInstanceUiState.Success
        requireNotNull(state.selectedImageId)
        requireNotNull(state.selectedInstanceType)

        createInstanceUseCase(
            name = state.name,
            imageId = state.selectedImageId,
            instanceType = state.selectedInstanceType,
        )
    }
}

@Immutable
internal sealed interface CreateInstanceUiState {
    data object Loading : CreateInstanceUiState

    data class Success(
        val images: List<Image>,
        val name: String = "",
        val selectedImageId: String,
        val selectedInstanceType: InstanceType = InstanceType.T2Micro,
    ) : CreateInstanceUiState
}
