package kr.ac.cbnu.software.aws_control.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import aws.sdk.kotlin.services.ec2.model.InstanceState
import aws.sdk.kotlin.services.ec2.model.InstanceType
import aws.sdk.kotlin.services.ec2.model.MonitoringState
import kr.ac.cbnu.software.aws_control.R

@Composable
fun InstanceItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    leadingContent: @Composable (() -> Unit),
    name: String,
    instanceId: String?,
    imageId: String?,
    instanceType: InstanceType?,
    instanceState: InstanceState?,
    monitoringState: MonitoringState?,
    onClick: (() -> Unit)?,
) {

    ListItem(
        modifier = (if (onClick != null) modifier.clickable(
            onClick = onClick
        ) else modifier).background(
            if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        leadingContent = leadingContent,
        headlineContent = {
            Text(text = name)
        },
        supportingContent = {
            Column {
                instanceId?.let {
                    Text("${stringResource(R.string.id)}: $it")
                }
                imageId?.let {
                    Text("${stringResource(R.string.image_id)}: $it")
                }
                instanceType?.let {
                    Text("${stringResource(R.string.instance_type)}: ${it.value}")
                }
                instanceState?.let {
                    Text("${stringResource(R.string.instance_state)}: ${it.name}")
                }
                monitoringState?.let {
                    Text("${stringResource(R.string.monitoring_state)}: ${it.value}")
                }
            }
        },
        trailingContent = {
            Text(instanceState?.name?.value ?: "")
        },
    )
}