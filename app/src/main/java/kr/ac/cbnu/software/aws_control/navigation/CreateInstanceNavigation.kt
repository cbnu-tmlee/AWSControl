package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.CreateInstanceScreen

@Serializable
internal object CreateInstanceRoute

internal fun NavController.navigateToCreateInstance(
    navOptions: NavOptions? = null
) {
    navigate(
        route = CreateInstanceRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.createInstanceScreen(navController: NavController) {
    composable<CreateInstanceRoute> {
        CreateInstanceScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
