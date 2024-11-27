package kr.ac.cbnu.software.aws_control.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.ec2.model.Image
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.ImageItem
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.ImageListUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.ImageListViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun ImageListScreen(
    viewModel: ImageListViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    ImageListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageListScreen(
    modifier: Modifier = Modifier,
    uiState: ImageListUiState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Text(text = stringResource(R.string.list_images))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState is ImageListUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                ImageListUiState.Loading ->
                    CircularProgressIndicator()

                is ImageListUiState.Success ->
                    ImageListContent(
                        images = uiState.images,
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageListContent(
    modifier: Modifier = Modifier,
    images: List<Image>,
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollableState,
    ) {
        images.forEach { image ->
            item(key = image.imageId) {
                ImageItem(
                    name = image.name ?: "",
                    imageId = image.imageId,
                    ownerId = image.ownerId,
                )
            }
        }
    }
}
