package org.tidy.feature_clients.presentation.register_client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterClientScreen(
    viewModel: RegisterClientViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onAction(RegisterClientAction.OnRegisterClick) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormValid
            ) {
                Text("Cadastrar")
            }
        }
    }
}