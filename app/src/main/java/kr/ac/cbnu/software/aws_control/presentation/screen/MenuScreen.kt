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
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuScreen(
    onInstanceListClick: (InstanceListScreenType) -> Unit,
    onAvailabilityZoneListClick: () -> Unit,
    onRegionListClick: () -> Unit,
    onCreateInstanceClick: () -> Unit,
    onImageListClick: () -> Unit,
    onSendCommandClick: (SendCommandScreenType) -> Unit,
    onSendFileClick: () -> Unit,
    onConsoleOutputClick: () -> Unit,
    onConsoleScreenshotClick: () -> Unit,
    onCloudWatchClick: () -> Unit,
    onCreateMetricAlarmClick: () -> Unit,
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
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.htcondor_icon),
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(R.string.condor_status),
                            )
                        },
                        headlineContent = {
                            Text("10. ${stringResource(R.string.condor_status)}")
                        },
                        onClick = { onSendCommandClick(SendCommandScreenType.CONDOR_STATUS) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.htcondor_icon),
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(R.string.condor_submit),
                            )
                        },
                        headlineContent = {
                            Text("11. ${stringResource(R.string.condor_submit)}")
                        },
                        onClick = { onSendCommandClick(SendCommandScreenType.CONDOR_SUBMIT) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.htcondor_icon),
                                modifier = Modifier.size(24.dp),
                                contentDescription = stringResource(R.string.condor_q),
                            )
                        },
                        headlineContent = {
                            Text("12. ${stringResource(R.string.condor_q)}")
                        },
                        onClick = { onSendCommandClick(SendCommandScreenType.CONDOR_Q) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_terminal_24),
                                contentDescription = stringResource(R.string.send_command),
                            )
                        },
                        headlineContent = {
                            Text("13. ${stringResource(R.string.send_command)}")
                        },
                        onClick = { onSendCommandClick(SendCommandScreenType.DEFAULT) },
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_insert_drive_file_24),
                                contentDescription = stringResource(R.string.send_file),
                            )
                        },
                        headlineContent = {
                            Text("14. ${stringResource(R.string.send_file)}")
                        },
                        onClick = onSendFileClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_notes_24),
                                contentDescription = stringResource(R.string.console_output),
                            )
                        },
                        headlineContent = {
                            Text("15. ${stringResource(R.string.console_output)}")
                        },
                        onClick = onConsoleOutputClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_camera_alt_24),
                                contentDescription = stringResource(R.string.instance_screenshot),
                            )
                        },
                        headlineContent = {
                            Text("14. ${stringResource(R.string.instance_screenshot)}")
                        },
                        onClick = onConsoleScreenshotClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_insert_chart_24),
                                contentDescription = stringResource(R.string.metric_data),
                            )
                        },
                        headlineContent = {
                            Text("16. ${stringResource(R.string.metric_data)}")
                        },
                        onClick = onCloudWatchClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_add_alarm_24),
                                contentDescription = stringResource(R.string.create_metric_alarm),
                            )
                        },
                        headlineContent = {
                            Text("17. ${stringResource(R.string.create_metric_alarm)}")
                        },
                        onClick = onCreateMetricAlarmClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_alarm_off_24),
                                contentDescription = stringResource(R.string.delete_metric_alarm),
                            )
                        },
                        headlineContent = {
                            Text("18. ${stringResource(R.string.delete_metric_alarm)}")
                        },
                        onClick = onCloudWatchClick,
                    )
                }
                item {
                    MenuItem(
                        leadingContent = {
                            Icon(
                                painterResource(R.drawable.baseline_directions_run_24),
                                contentDescription = stringResource(R.string.quit),
                            )
                        },
                        headlineContent = {
                            Text("99. ${stringResource(R.string.quit)}")
                        },
                        onClick = {
                            exitProcess(0)
                        },
                    )
                }
            },
        )
    }
}
