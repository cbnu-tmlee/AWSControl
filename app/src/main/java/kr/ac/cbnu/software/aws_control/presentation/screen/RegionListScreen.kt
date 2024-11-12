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
import aws.sdk.kotlin.services.ec2.model.Region
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.RegionItem
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.RegionListUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.RegionListViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun RegionListScreen(
    viewModel: RegionListViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    RegionListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegionListScreen(
    modifier: Modifier = Modifier,
    uiState: RegionListUiState,
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
                    Text(stringResource(R.string.available_regions))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState is RegionListUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                RegionListUiState.Loading ->
                    CircularProgressIndicator()

                is RegionListUiState.Success ->
                    RegionListContent(
                        regions = uiState.regions,
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegionListContent(
    modifier: Modifier = Modifier,
    regions: List<Region>,
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollableState,
    ) {
        regions.forEach { region ->
            item(key = region.regionName) {
                RegionItem(
                    name = region.regionName ?: "",
                    endPoint = region.endpoint,
                )
            }
        }
    }
}
