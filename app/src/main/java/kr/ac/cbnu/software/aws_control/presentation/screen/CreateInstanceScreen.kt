package kr.ac.cbnu.software.aws_control.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aws.sdk.kotlin.services.ec2.model.Image
import aws.sdk.kotlin.services.ec2.model.InstanceType
import kr.ac.cbnu.software.aws_control.R
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.CreateInstanceUiState
import kr.ac.cbnu.software.aws_control.presentation.viewmodel.CreateInstanceViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun CreateInstanceScreen(
    viewModel: CreateInstanceViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState = viewModel.collectAsState().value

    CreateInstanceScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onNameChange = viewModel::setName,
        onImageSelectionChange = viewModel::selectImage,
        onInstanceTypeSelectionChange = viewModel::selectInstanceType,
        onCreateClick = {
            viewModel.runInstance()
            onBackClick()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateInstanceScreen(
    modifier: Modifier = Modifier,
    uiState: CreateInstanceUiState,
    onBackClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onImageSelectionChange: (String) -> Unit,
    onInstanceTypeSelectionChange: (InstanceType) -> Unit,
    onCreateClick: () -> Unit,
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
                    Text(
                        text = stringResource(R.string.create_instance)
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateClick,
                icon = {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = stringResource(R.string.create),
                    )
                },
                text = {
                    Text(text = stringResource(R.string.create))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (uiState is CreateInstanceUiState.Loading) Arrangement.Center else Arrangement.Top,
        ) {
            when (uiState) {
                CreateInstanceUiState.Loading ->
                    CircularProgressIndicator()

                is CreateInstanceUiState.Success ->
                    InstanceContent(
                        images = uiState.images,
                        name = uiState.name,
                        selectedImageId = uiState.selectedImageId,
                        selectedInstanceType = uiState.selectedInstanceType,
                        onNameChange = onNameChange,
                        onImageSelectionChange = onImageSelectionChange,
                        onInstanceTypeSelectionChange = onInstanceTypeSelectionChange,
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstanceContent(
    modifier: Modifier = Modifier,
    images: List<Image>,
    name: String,
    selectedImageId: String,
    selectedInstanceType: InstanceType,
    onNameChange: (String) -> Unit,
    onImageSelectionChange: (String) -> Unit,
    onInstanceTypeSelectionChange: (InstanceType) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = name,
            onValueChange = onNameChange,
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.baseline_notes_24),
                    contentDescription = stringResource(R.string.name),
                )
            },
            label = {
                Text(text = stringResource(R.string.name))
            },
        )
        OptionBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            options = images.filter { it.name != null }.map { it.name!! },
            selectedOption = images.find { it.imageId == selectedImageId }!!.name!!,
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.baseline_save_24),
                    contentDescription = stringResource(R.string.image),
                )
            },
            label = {
                Text(text = stringResource(R.string.image))
            },
            onSelectionChange = { selectedImageName ->
                val imageId = images.find { it.name == selectedImageName }!!.imageId!!
                onImageSelectionChange(imageId)
            },
        )
        OptionBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            options = InstanceType.values().filter { it != null }.map { it.value },
            selectedOption = selectedInstanceType.value,
            leadingIcon = {
                Icon(
                    painterResource(R.drawable.baseline_dns_24),
                    contentDescription = stringResource(R.string.instance_type),
                )
            },
            label = {
                Text(text = stringResource(R.string.instance_type))
            },
            onSelectionChange = {
                val instanceType = InstanceType.fromValue(it)
                onInstanceTypeSelectionChange(instanceType)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionBox(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit),
    label: @Composable (() -> Unit),
    options: List<String>,
    selectedOption: String,
    onSelectionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = TextFieldValue(selectedOption),
            onValueChange = { onSelectionChange(it.text) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            singleLine = true,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
