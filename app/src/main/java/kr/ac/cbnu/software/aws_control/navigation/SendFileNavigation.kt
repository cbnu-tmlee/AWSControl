package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.SendFileScreen

@Serializable
internal object SendFileRoute

internal fun NavController.navigateToSendFile(
    navOptions: NavOptions? = null
) {
    navigate(
        route = SendFileRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.sendFileScreen(navController: NavController) {
    composable<SendFileRoute> {
        SendFileScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
