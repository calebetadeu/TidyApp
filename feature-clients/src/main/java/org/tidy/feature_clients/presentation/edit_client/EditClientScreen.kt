package org.tidy.feature_clients.presentation.edit_client

import android.Manifest
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.presentation.clients_list.components.CityDropdownWithSearch
import org.tidy.feature_clients.presentation.clients_list.components.StateDropdown
import org.tidy.feature_clients.presentation.clients_list.components.getCurrentLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClientScreen(
    clientId: String,
    viewModel: EditClientViewModel = koinViewModel(),
    locations: List<LocationDto>,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
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
    var selectedState by remember { mutableStateOf(state.estado) }
    var selectedCity by remember { mutableStateOf(state.cidade) }
   // val locations = viewModel.locations

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
                title = { Text("Editar Cliente") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onAction(EditClientAction.SyncClient) }) {
                        Icon(Icons.Default.CloudSync, contentDescription = "Sincronizar")
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
                EditTextField("Razão Social", state.razaoSocial) {
                    viewModel.onAction(EditClientAction.OnRazaoSocialChange(it))
                }

                EditTextField("Nome Fantasia", state.nomeFantasia) {
                    viewModel.onAction(EditClientAction.OnNomeFantasiaChange(it))
                }

                EditTextField("CNPJ", state.cnpj) {
                    viewModel.onAction(EditClientAction.OnCnpjChange(it))
                }
                StateDropdown(
                    locations = locations,
                    selectedState = selectedState,
                    defaultState = state.estado,
                    onStateSelected = { newState->
                        selectedState = newState
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CityDropdownWithSearch(
                    locations = locations,
                    selectedState = selectedState,
                    selectedCity = selectedCity,
                    defaultCity = state.cidade,
                    onCitySelected = { city ->
                        selectedCity = city
                    }
                )

                EditTextField("Rota", state.rota) {
                    viewModel.onAction(EditClientAction.OnRotaChange(it))
                }

                EditTextField("Localização", state.localizacao) {
                    viewModel.onAction(EditClientAction.OnLocalizacaoChange(it))
                }

                Button(onClick = {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Text("Usar Localização Atual")
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
                            onCheckedChange = { isSelected ->
                                val updatedList = if (isSelected) {
                                    state.empresasTrabalhadas + empresa
                                } else {
                                    state.empresasTrabalhadas - empresa
                                }
                                viewModel.onAction(EditClientAction.OnEmpresasTrabalhadasChange(updatedList))
                            }
                        )
                        Text(text = empresa, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Button(
                    onClick = {
                        viewModel.onAction(EditClientAction.OnEstadoChange(selectedState))
                        viewModel.onAction(EditClientAction.OnCidadeChange(selectedCity))
                        viewModel.onAction(EditClientAction.SaveClient)
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar Alterações")
                }
            }
        }
    )
}
// 🚀 **Campo Editável Padrão**
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                label = { Text(label) },
                enabled = true,
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
}


@Preview
@Composable
fun EditClientScreenPreview() {
    EditClientScreen(clientId = "", onNavigateBack = {}, locations = listOf(LocationDto(
        listaCidades = listOf("São Paulo", "Rio de Janeiro", "Belo Horizonte")
    )))
}