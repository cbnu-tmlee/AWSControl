package kr.ac.cbnu.software.aws_control.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import aws.sdk.kotlin.services.ec2.model.AvailabilityZone
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.ac.cbnu.software.aws_control.domain.usecase.GetAvailabilityZonesUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class AvailabilityZoneListViewModel @Inject constructor(
    private val getAvailabilityZonesUseCase: GetAvailabilityZonesUseCase
) : ContainerHost<AvailabilityZoneListUiState, Nothing>, ViewModel() {
    override val container =
        container<AvailabilityZoneListUiState, Nothing>(initialState = AvailabilityZoneListUiState.Loading) {
            loadAvailabilityZones()
        }

    private fun loadAvailabilityZones() = intent(registerIdling = false) {
        repeatOnSubscription {
            val availabilityZones: List<AvailabilityZone> = getAvailabilityZonesUseCase()

            reduce {
                AvailabilityZoneListUiState.Success(
                    availabilityZones = availabilityZones
                )
            }
        }
    }
}

@Immutable
internal sealed interface AvailabilityZoneListUiState {
    data object Loading : AvailabilityZoneListUiState

    data class Success(
        val availabilityZones: List<AvailabilityZone>
    ) : AvailabilityZoneListUiState
}
