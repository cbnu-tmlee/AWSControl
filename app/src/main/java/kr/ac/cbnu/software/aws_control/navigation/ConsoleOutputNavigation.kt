package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.ConsoleOutputScreen

@Serializable
internal object ConsoleOutputRoute

internal fun NavController.navigateToConsoleOutput(
    navOptions: NavOptions? = null
) {
    navigate(
        route = ConsoleOutputRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.consoleOutputScreen(navController: NavController) {
    composable<ConsoleOutputRoute> {
        ConsoleOutputScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
