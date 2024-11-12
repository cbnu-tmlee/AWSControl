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
    }
}
