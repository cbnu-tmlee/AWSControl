package kr.ac.cbnu.software.aws_control.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun MenuItem(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit),
    headlineContent: @Composable (() -> Unit),
    onClick: () -> Unit,
) {
    ListItem(
        leadingContent = leadingContent,
        headlineContent = headlineContent,
        modifier = modifier.clickable(
            enabled = true,
            onClick = onClick,
        ),
    )
}