package org.tidy.feature_clients.presentation.edit_client

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import org.koin.androidx.compose.koinViewModel
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.model.Localization
import org.tidy.feature_clients.presentation.components.CityDropdownWithSearch
import org.tidy.feature_clients.presentation.components.StateDropdown
import org.tidy.feature_clients.presentation.components.getCurrentLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClientScreen(
    clientId: String,
    viewModel: EditClientViewModel = koinViewModel(),
    locations: List<LocationDto>,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val empresasDisponiveis = listOf(
        "Casa Dos Rolamentos",
        "Ditrator",
        "Indagril",
        "Agromann",
        "Romar Mann",
        "Primus",
        "Smart Crops"
    )

    // Estados para seleção de estado e cidade
    var selectedState by remember { mutableStateOf(state.estado) }
    var selectedCity by remember { mutableStateOf(state.cidade) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(viewModel, context)
        }
    }

    LaunchedEffect(clientId) {
        viewModel.onAction(EditClientAction.LoadClient(clientId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Cliente" else "Detalhes do Cliente") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar"
                            )
                        }
                    }
                    IconButton(onClick = { viewModel.onAction(EditClientAction.SyncClient) }) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Sincronizar"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                EditTextField(
                    label = "Razão Social",
                    value = state.razaoSocial,
                    onValueChange = { viewModel.onAction(EditClientAction.OnRazaoSocialChange(it)) },
                    enabled = isEditing
                )
                EditTextField(
                    label = "Nome Fantasia",
                    value = state.nomeFantasia,
                    onValueChange = { viewModel.onAction(EditClientAction.OnNomeFantasiaChange(it)) },
                    enabled = isEditing
                )
                EditTextField(
                    label = "CNPJ",
                    value = state.cnpj,
                    onValueChange = { viewModel.onAction(EditClientAction.OnCnpjChange(it)) },
                    enabled = isEditing
                )
                StateDropdown(
                    locations = locations,
                    selectedState = selectedState,
                    defaultState = state.estado,
                    onStateSelected = { newState -> selectedState = newState }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CityDropdownWithSearch(
                    locations = locations,
                    selectedState = selectedState,
                    selectedCity = selectedCity,
                    defaultCity = state.cidade,
                    onCitySelected = { city -> selectedCity = city }
                )
                EditTextField(
                    label = "Rota",
                    value = state.rota,
                    onValueChange = { viewModel.onAction(EditClientAction.OnRotaChange(it)) },
                    enabled = isEditing
                )
                EditTextField(
                    label = "Localização",
                    value = "${state.localizacao?.latitude}, ${state.localizacao?.longitude}",
                    onValueChange = { input ->
                        val parts = input.split(",")
                        if (parts.size == 2) {
                            viewModel.onAction(
                                EditClientAction.OnLocalizacaoChange(
                                    Localization(
                                        latitude = parts[0].trim().toDoubleOrNull() ?: 0.0,
                                        longitude = parts[1].trim().toDoubleOrNull() ?: 0.0
                                    )
                                )
                            )
                        }
                    },
                    enabled = isEditing
                )
                if(state.localizacao == null || state.localizacao != Localization(0.0, 0.0)  && !isEditing){
                    Button(
                        onClick = {
                            state.localizacao?.let { loc ->
                                // Cria a URI no formato 'geo:'
                                val uri =
                                    "geo:${loc.latitude},${loc.longitude}?q=${loc.latitude},${loc.longitude}(${state.razaoSocial})".toUri()
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                // Inicia o intent para abrir o Maps
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Abrir no Maps")
                    }
                }
                if(isEditing){
                    Button(onClick = {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }) {
                        Text("Usar Localização Atual")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Empresas Trabalhadas", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                empresasDisponiveis.forEach { empresa ->
                    val isChecked = state.empresasTrabalhadas.contains(empresa)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = if (isEditing) { isSelected ->
                                val updatedList = if (isSelected) {
                                    state.empresasTrabalhadas + empresa
                                } else {
                                    state.empresasTrabalhadas - empresa
                                }
                                viewModel.onAction(
                                    EditClientAction.OnEmpresasTrabalhadasChange(updatedList)
                                )
                            } else null
                        )
                        Text(text = empresa, modifier = Modifier.padding(start = 8.dp))
                    }
                }
                if (isEditing) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Atualiza os campos de estado e cidade e salva as alterações
                            viewModel.onAction(EditClientAction.OnEstadoChange(selectedState))
                            viewModel.onAction(EditClientAction.OnCidadeChange(selectedCity))
                            viewModel.onAction(EditClientAction.SaveClient)
                            isEditing = false
                            onNavigateBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar Alterações")
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    if (enabled) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    label = { Text(label) },
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                        start = 0.dp,
                        end = 0.dp
                    )
                )
            }
        )
    } else {
        // Modo somente leitura: exibe o label e o valor
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
