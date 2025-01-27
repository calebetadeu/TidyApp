package org.tidy.feature_clients.presentation.register_client

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.tidy.feature_clients.presentation.clients_list.components.getCurrentLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterClientScreen(
    viewModel: RegisterClientViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToClientList: () -> Unit // 🔥 Nova função para redirecionar após registro bem-sucedido
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 🚀 **Solicitação de Permissão de Localização**
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(viewModel, context)
        }
    }

    LaunchedEffect(state.successMessage, state.errorMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            onNavigateToClientList() // 🔥 Redireciona para a listagem de clientes após sucesso
        }
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastrar Cliente") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) } // 🔥 Exibe Snackbar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // 🔥 Habilita Scroll
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.razaoSocial,
                onValueChange = { viewModel.onAction(RegisterClientAction.OnRazaoSocialChange(it)) },
                label = { Text("Razão Social") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.nomeFantasia,
                onValueChange = { viewModel.onAction(RegisterClientAction.OnNomeFantasiaChange(it)) },
                label = { Text("Nome Fantasia") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.cnpj,
                onValueChange = { viewModel.onAction(RegisterClientAction.OnCnpjChange(it)) },
                label = { Text("CNPJ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.cidade,
                onValueChange = { viewModel.onAction(RegisterClientAction.OnCidadeChange(it)) },
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.estado,
                onValueChange = { viewModel.onAction(RegisterClientAction.OnEstadoChange(it)) },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🚀 **Campo para selecionar a Rota**
            OutlinedTextField(
                value = state.rota,
                onValueChange = { viewModel.onAction(RegisterClientAction.OnRotaChange(it)) },
                label = { Text("Rota") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🚀 **Campo de Localização com opção de localização atual**
            OutlinedTextField(
                value = state.localizacao,
                onValueChange = { viewModel.onAction(RegisterClientAction.OnLocalizacaoChange(it)) },
                label = { Text("Localização") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Usar Localização Atual")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🚀 **Checkboxes para Empresas Trabalhadas**
            Text("Empresas Trabalhadas", style = MaterialTheme.typography.titleMedium)

            state.listaEmpresas.forEach { empresa ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = empresa in state.empresasTrabalhadas,
                        onCheckedChange = {
                            viewModel.onAction(RegisterClientAction.OnEmpresasTrabalhadasChange(empresa, it))
                        }
                    )
                    Text(text = empresa, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.onAction(RegisterClientAction.OnRegisterClick)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormValid
            ) {
                Text("Cadastrar")
            }
        }
    }
}