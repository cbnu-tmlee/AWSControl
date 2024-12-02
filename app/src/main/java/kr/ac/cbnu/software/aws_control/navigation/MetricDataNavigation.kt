package kr.ac.cbnu.software.aws_control.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kr.ac.cbnu.software.aws_control.presentation.screen.MetricDataScreen

@Serializable
internal object MetricDataRoute

internal fun NavController.navigateToMetricData(
    navOptions: NavOptions? = null
) {
    navigate(
        route = MetricDataRoute,
        navOptions = navOptions
    )
}

internal fun NavGraphBuilder.metricDataScreen(navController: NavController) {
    composable<MetricDataRoute> {
        MetricDataScreen(
            onBackClick = navController::popBackStack,
        )
    }
}
