package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.InstanceListScreen
import kr.ac.cbnu.software.aws_control.presentation.screen.InstanceListScreenType

@Serializable
internal data class InstanceListRoute(
    val screenType: InstanceListScreenType
)

internal fun NavController.navigateToInstanceList(
    screenType: InstanceListScreenType,
    navOptions: NavOptions? = null
) {
    navigate(
        route = InstanceListRoute(screenType = screenType),
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.instanceListScreen(navController: NavController) {
    composable<InstanceListRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<InstanceListRoute>()

        InstanceListScreen(
            screenType = route.screenType,
            onBackClick = navController::popBackStack,
        )
    }
}
