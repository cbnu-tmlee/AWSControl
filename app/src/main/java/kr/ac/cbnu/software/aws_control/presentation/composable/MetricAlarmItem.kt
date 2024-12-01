package kr.ac.cbnu.software.aws_control.presentation.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kr.ac.cbnu.software.aws_control.R

@Composable
fun MetricAlarmItem(
    modifier: Modifier = Modifier,
    name: String,
    description: String?,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                painterResource(R.drawable.baseline_access_alarm_24),
                contentDescription = null
            )
        },
        headlineContent = {
            Text(text = name)
        },
        supportingContent = {
            description?.let {
                Text(text = "${stringResource(R.string.description)}: $it")
            }
        }
    )
}
