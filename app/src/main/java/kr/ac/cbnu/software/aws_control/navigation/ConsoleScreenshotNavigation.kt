package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.ConsoleScreenshotScreen

@Serializable
internal object ConsoleScreenshotRoute

internal fun NavController.navigateToConsoleScreenshot(
    navOptions: NavOptions? = null
) {
    navigate(
        route = ConsoleScreenshotRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.consoleScreenshotScreen(navController: NavController) {
    composable<ConsoleScreenshotRoute> {
        ConsoleScreenshotScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
