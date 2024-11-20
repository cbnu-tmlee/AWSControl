package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.SendCommandScreen
import kr.ac.cbnu.software.aws_control.presentation.screen.SendCommandScreenType

@Serializable
internal data class SendCommandRoute(
    val screenType: SendCommandScreenType
)

internal fun NavController.navigateToSendCommand(
    screenType: SendCommandScreenType,
    navOptions: NavOptions? = null
) {
    navigate(
        route = SendCommandRoute(screenType = screenType),
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.sendCommandScreen(navController: NavController) {
    composable<SendCommandRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SendCommandRoute>()

        SendCommandScreen(
            screenType = route.screenType,
            onBackClick = navController::popBackStack,
        )
    }
}
