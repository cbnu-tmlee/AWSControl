package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.RegionListScreen

@Serializable
internal object RegionListRoute

internal fun NavController.navigateToRegionList(
    navOptions: NavOptions? = null
) {
    navigate(
        route = RegionListRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.regionListScreen(navController: NavController) {
    composable<RegionListRoute> {
        RegionListScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
