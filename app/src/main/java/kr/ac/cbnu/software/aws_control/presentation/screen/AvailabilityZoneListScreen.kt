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
import aws.sdk.kotlin.services.ec2.model.AvailabilityZone
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.AvailabilityZoneItem
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.AvailabilityZoneListUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.AvailabilityZoneListViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun AvailabilityZoneListScreen(
    viewModel: AvailabilityZoneListViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    AvailabilityZoneListScreen(
        uiState = uiState,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvailabilityZoneListScreen(
    modifier: Modifier = Modifier,
    uiState: AvailabilityZoneListUiState,
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
                    Text(text = stringResource(R.string.available_zones))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState is AvailabilityZoneListUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                AvailabilityZoneListUiState.Loading ->
                    CircularProgressIndicator()

                is AvailabilityZoneListUiState.Success ->
                    AvailabilityZoneListContent(
                        availabilityZones = uiState.availabilityZones,
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvailabilityZoneListContent(
    modifier: Modifier = Modifier,
    availabilityZones: List<AvailabilityZone>,
) {
    val scrollableState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollableState,
    ) {
        availabilityZones.forEach { availabilityZone ->
            item(key = availabilityZone.zoneId) {
                AvailabilityZoneItem(
                    name = availabilityZone.zoneName ?: "",
                    zoneId = availabilityZone.zoneId,
                    regionName = availabilityZone.regionName,
                )
            }
        }
    }
}
