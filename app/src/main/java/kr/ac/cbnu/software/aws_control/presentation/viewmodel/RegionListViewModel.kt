package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetRegionsUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class RegionListViewModel @Inject constructor(
    private val getRegionsUseCase: GetRegionsUseCase
) : ContainerHost<RegionListUiState, Nothing>, ViewModel() {
    override val container =
        container<RegionListUiState, Nothing>(initialState = RegionListUiState.Loading) {
            loadRegions()
        }

    private fun loadRegions() = intent(registerIdling = false) {
        repeatOnSubscription {
            val regions = getRegionsUseCase()

            reduce {
                RegionListUiState.Success(
                    regions = regions
                )
            }
        }
    }
}

@Immutable
internal sealed interface RegionListUiState {
    data object Loading : RegionListUiState

    data class Success(
        val regions: List<Region>
    ) : RegionListUiState
}
