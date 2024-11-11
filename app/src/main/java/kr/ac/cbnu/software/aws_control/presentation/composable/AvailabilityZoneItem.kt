package kr.ac.cbnu.software.aws_control.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kr.ac.cbnu.software.aws_control.R

@Composable
fun AvailabilityZoneItem(
    modifier: Modifier = Modifier,
    name: String,
    zoneId: String?,
    regionName: String?,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(text = name)
        },
        supportingContent = {
            Column {
                zoneId?.let {
                    Text(text = "${stringResource(R.string.id)}: $it")
                }
                regionName?.let {
                    Text(text = "${stringResource(R.string.region_name)}: $it")
                }
            }
        },
    )
}