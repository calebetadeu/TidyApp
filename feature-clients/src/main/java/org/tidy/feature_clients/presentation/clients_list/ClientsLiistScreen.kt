package org.tidy.feature_clients.presentation.clients_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.presentation.clients_list.components.FilterComponent

@Composable
fun ClientListScreen(
    viewModel: ClientListViewModel = koinViewModel(),
    onNavigateToEditClient: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { ClientListTopBar(viewModel) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // 🔹 Filtro de Clientes (Rota, Cidade, Empresa)
                FilterComponent(
                    rota = state.rotaFilter,
                    cidade = state.cidadeFilter,
                    empresa = state.empresaFilter,
                    onFilterChange = { rota, cidade, empresa ->
                        viewModel.onAction(ClientListAction.FilterClients(rota, cidade, empresa))
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Lista de Clientes
                when {
                    state.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    state.clients.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Nenhum cliente encontrado.")
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(state.clients) { client ->
                                ClientItem(client) {
                                    onNavigateToEditClient(client.codigoTidy) // 🔥 Passa apenas o ID do cliente
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListTopBar(viewModel: ClientListViewModel) {
    TopAppBar(
        title = { Text("Lista de Clientes") },
        actions = {
            IconButton(onClick = { viewModel.onAction(ClientListAction.SyncClients) }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Sincronizar")
            }
        }
    )
}

@Composable
fun ClientItem(client: Client, onNavigateToEditClient: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNavigateToEditClient(client.codigoTidy) } // 🔥 Agora abre a edição pelo ID
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = client.razaoSocial, style = MaterialTheme.typography.titleMedium)
            Text(text = "${client.cidade} - ${client.estado}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Rota: ${client.rota}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun ClientListScreenPreview() {
    ClientListScreen(onNavigateToEditClient = {})
}