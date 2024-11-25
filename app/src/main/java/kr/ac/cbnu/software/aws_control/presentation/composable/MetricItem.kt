package kr.ac.cbnu.software.aws_control.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kr.ac.cbnu.software.aws_control.R

@Composable
fun MetricItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    instanceId: String,
    metricName: String,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = modifier.clickable(
            onClick = onClick,
        ).background(
            if (selected)
                Color.Yellow
            else
                Color.Transparent
            ),
        leadingContent = {
            Icon(
                painterResource(R.drawable.baseline_insert_chart_24),
                contentDescription = null
            )
        },
        headlineContent = {
            Text(instanceId)
        },
        supportingContent = {
            Text("${stringResource(R.string.metric)}: $metricName")
        }
    )
}