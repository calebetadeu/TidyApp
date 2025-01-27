package org.tidy.feature_clients.presentation.edit_client

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import org.koin.androidx.compose.koinViewModel
import org.tidy.feature_clients.presentation.clients_list.components.getCurrentLocation
import org.tidy.feature_clients.presentation.register_client.RegisterClientAction
import org.tidy.feature_clients.presentation.register_client.RegisterClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClientScreen(
    clientId: Int,
    viewModel: EditClientViewModel = koinViewModel(),
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
        "Primus"
    )

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

                EditTextField("Cidade", state.cidade) {
                    viewModel.onAction(EditClientAction.OnCidadeChange(it))
                }

                EditTextField("Estado", state.estado) {
                    viewModel.onAction(EditClientAction.OnEstadoChange(it))
                }

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
    EditClientScreen(clientId = 123, onNavigateBack = {})
}