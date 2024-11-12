package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.AvailabilityZoneListScreen

@Serializable
internal object AvailabilityZoneListRoute

internal fun NavController.navigateToAvailabilityZoneList(
    navOptions: NavOptions? = null
) {
    navigate(
        route = AvailabilityZoneListRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.availabilityZoneListScreen(navController: NavController) {
    composable<AvailabilityZoneListRoute> {
        AvailabilityZoneListScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
