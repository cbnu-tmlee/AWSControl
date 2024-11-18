package kr.ac.cbnu.software.aws_control.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.composable.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuScreen(
    onInstanceListClick: (InstanceListScreenType) -> Unit,
    onAvailabilityZoneListClick: () -> Unit,
    onRegionListClick: () -> Unit,
    onCreateInstanceClick: () -> Unit,
    onImageListClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Fixed(count = 2),
            content = {
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_dns_24),
                                contentDescription = stringResource(R.string.list_instances),
                            )
                        },
                        headlineContent = {
                            Text("1. ${stringResource(R.string.list_instances)}")
                        },
                        onClick = { onInstanceListClick(InstanceListScreenType.LIST) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = stringResource(R.string.available_zones),
                            )
                        },
                        headlineContent = {
                            Text("2. ${stringResource(R.string.available_zones)}")
                        },
                        onClick = onAvailabilityZoneListClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = stringResource(R.string.start_instances),
                            )
                        },
                        headlineContent = {
                            Text("3. ${stringResource(R.string.start_instances)}")
                        },
                        onClick = { onInstanceListClick(InstanceListScreenType.START) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = stringResource(R.string.available_regions),
                            )
                        },
                        headlineContent = {
                            Text("4. ${stringResource(R.string.available_regions)}")
                        },
                        onClick = onRegionListClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_stop_24),
                                contentDescription = stringResource(R.string.stop_instances),
                            )
                        },
                        headlineContent = {
                            Text("5. ${stringResource(R.string.stop_instances)}")
                        },
                        onClick = { onInstanceListClick(InstanceListScreenType.STOP) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_dns_24),
                                contentDescription = stringResource(R.string.create_instance),
                            )
                        },
                        headlineContent = {
                            Text("6. ${stringResource(R.string.create_instance)}")
                        },
                        onClick = onCreateInstanceClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.reboot_instance),
                            )
                        },
                        headlineContent = {
                            Text("7. ${stringResource(R.string.reboot_instance)}")
                        },
                        onClick = { onInstanceListClick(InstanceListScreenType.REBOOT) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_save_24),
                                contentDescription = stringResource(R.string.list_images),
                            )
                        },
                        headlineContent = {
                            Text("8. ${stringResource(R.string.list_images)}")
                        },
                        onClick = onImageListClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_power_settings_new_24),
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(R.string.terminate_instances),
                            )
                        },
                        headlineContent = {
                            Text("9. ${stringResource(R.string.terminate_instances)}")
                        },
                        onClick = { onInstanceListClick(InstanceListScreenType.TERMINATE) },
                    )
                }
            },
        )
    }
}
