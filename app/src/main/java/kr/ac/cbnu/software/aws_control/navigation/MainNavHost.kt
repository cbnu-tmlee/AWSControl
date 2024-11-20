package kr.ac.cbnu.software.aws_control.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
internal fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MenuRoute,
        modifier = modifier,
    ) {
        menuScreen(
            onInstanceListClick = {
                navController.navigateToInstanceList(screenType = it)
            },
            onAvailabilityZoneListClick = navController::navigateToAvailabilityZoneList,
            onRegionListClick = navController::navigateToRegionList,
            onCreateInstanceClick = navController::navigateToCreateInstance,
            onImageListClick = navController::navigateToImageList,
            onSendCommandClick = {
                navController.navigateToSendCommand(screenType = it)
            },
            onSendFileClick = navController::navigateToSendFile,
            onConsoleOutputClick = navController::navigateToConsoleOutput,
            onConsoleScreenshotClick = navController::navigateToConsoleScreenshot,
        )

        instanceListScreen(
            navController = navController,
        )
        availabilityZoneListScreen(
            navController = navController,
        )
        regionListScreen(
            navController = navController,
        )
        createInstanceScreen(
            navController = navController,
        )
        imageListScreen(
            navController = navController,
        )

        sendCommandScreen(
            navController = navController,
        )
        sendFileScreen(
            navController = navController,
        )
        consoleOutputScreen(
            navController = navController,
        )
        consoleScreenshotScreen(
            navController = navController,
        )
    }
}
