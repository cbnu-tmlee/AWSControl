package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.CreateMetricAlarmScreen

@Serializable
internal object CreateMetricAlarmRoute

internal fun NavController.navigateToCreateMetricAlarm(
    navOptions: NavOptions? = null
) {
    navigate(
        route = CreateMetricAlarmRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.createMetricAlarmScreen(navController: NavController) {
    composable<CreateMetricAlarmRoute> {
        CreateMetricAlarmScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
