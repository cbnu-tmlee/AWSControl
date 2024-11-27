package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetImagesUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class ImageListViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase
) : ContainerHost<ImageListUiState, Nothing>, ViewModel() {
    override val container =
        container<ImageListUiState, Nothing>(initialState = ImageListUiState.Loading) {
            loadImages()
        }

    private fun loadImages() = intent(registerIdling = false) {
        repeatOnSubscription {
            val images: List<Image> = getImagesUseCase()

            reduce {
                ImageListUiState.Success(
                    images = images
                )
            }
        }
    }
}

@Immutable
internal sealed interface ImageListUiState {
    data object Loading : ImageListUiState

    data class Success(
        val images: List<Image>
    ) : ImageListUiState
}
