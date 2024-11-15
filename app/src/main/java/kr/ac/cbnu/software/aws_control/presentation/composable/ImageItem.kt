package kr.ac.cbnu.software.aws_control.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kr.ac.cbnu.software.aws_control.R

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    name: String,
    imageId: String?,
    ownerId: String?,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                painterResource(R.drawable.baseline_save_24),
                contentDescription = null
            )
        },
        headlineContent = {
            Text(text = name)
        },
        supportingContent = {
            Column {
                imageId?.let {
                    Text(text = "${stringResource(R.string.image_id)}: $it")
                }
                ownerId?.let {
                    Text(text = "${stringResource(R.string.owner)}: $it")
                }
            }
        },
    )
}