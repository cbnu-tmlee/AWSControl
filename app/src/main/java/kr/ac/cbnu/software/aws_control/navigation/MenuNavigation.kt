package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.InstanceListScreenType
import kr.ac.cbnu.software.aws_control.presentation.screen.MenuScreen
import kr.ac.cbnu.software.aws_control.presentation.screen.SendCommandScreenType

@Serializable
internal object MenuRoute

internal fun NavGraphBuilder.menuScreen(
    onInstanceListClick: (InstanceListScreenType) -> Unit,
    onAvailabilityZoneListClick: () -> Unit,
    onRegionListClick: () -> Unit,
    onCreateInstanceClick: () -> Unit,
    onImageListClick: () -> Unit,
) {
    composable<MenuRoute> {
        MenuScreen(
            onInstanceListClick = onInstanceListClick,
            onAvailabilityZoneListClick = onAvailabilityZoneListClick,
            onRegionListClick = onRegionListClick,
            onCreateInstanceClick = onCreateInstanceClick,
            onImageListClick = onImageListClick,
        )
    }
}
