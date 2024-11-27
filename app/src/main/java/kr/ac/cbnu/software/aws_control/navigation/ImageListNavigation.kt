package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.ImageListScreen

@Serializable
internal object ImageListRoute

internal fun NavController.navigateToImageList(
    navOptions: NavOptions? = null
) {
    navigate(
        route = ImageListRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.imageListScreen(navController: NavController) {
    composable<ImageListRoute> {
        ImageListScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
