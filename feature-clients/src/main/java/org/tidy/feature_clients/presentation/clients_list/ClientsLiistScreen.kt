package org.tidy.feature_clients.presentation.clients_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.tidy.feature_clients.data.remote.LocationDto
import org.tidy.feature_clients.domain.model.Client
import org.tidy.feature_clients.presentation.clients_list.components.CityDropdownWithSearch
import org.tidy.feature_clients.presentation.clients_list.components.StateDropdown

@Composable
fun ClientListScreen(
    viewModel: ClientListViewModel = koinViewModel(),
    onNavigateToEditClient: ( String) -> Unit,
    locations:List<LocationDto>
) {
    // Estados dos filtros
    var selectedState by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var razaoSocial by remember { mutableStateOf("") }

    // Obtém as localizações (estados e cidades) do ViewModel
  //  val locations: List<LocationDto> = viewModel.locations

    // Coleta o fluxo paginado dos clientes
    val lazyPagingItems = viewModel.clientsPagingFlow.collectAsLazyPagingItems()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { ClientListTopBar(viewModel) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 🔹 Seção de Filtros
                Column(modifier = Modifier.padding(16.dp)) {
                    StateDropdown(
                        locations = locations,
                        selectedState = selectedState,
                        defaultState = "",
                        onStateSelected = { state ->
                            selectedState = state
                            selectedCity = ""
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CityDropdownWithSearch(
                        locations = locations,
                        selectedState = selectedState,
                        defaultCity = selectedCity,
                        selectedCity = selectedCity,
                        onCitySelected = { city -> selectedCity = city }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        enabled = false,//Implementar dps a logica de buscar razao social
                        value = razaoSocial,
                        onValueChange = { razaoSocial = it },
                        label = { Text("Razão Social") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            viewModel.onAction(
                                ClientListAction.FilterClients(
                                    estado = selectedState,
                                    cidade = selectedCity,
                                    razaoSocial = razaoSocial
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Aplicar Filtros")
                    }
                }

                // 🔹 Lista de Clientes
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                            CircularProgressIndicator()
                        }

                        lazyPagingItems.loadState.refresh is LoadState.NotLoading &&
                                lazyPagingItems.itemCount == 0 -> {
                            Text("Nenhum cliente encontrado.")
                        }

                        else -> {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(lazyPagingItems.itemCount) { index ->
                                    lazyPagingItems[index]?.let { client ->
                                        ClientItem(client) {
                                            onNavigateToEditClient(client.id.toString())
                                        }
                                    }
                                }
                                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                                    item {
                                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                    }
                                }
                                if (lazyPagingItems.loadState.append is LoadState.Error) {
                                    item {
                                        Text(
                                            "Erro ao carregar mais clientes.",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 🔹 Botão para "Carregar Mais"
                Button(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(lazyPagingItems.itemCount - 1)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Carregar Mais")
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
fun ClientItem(client: Client, onNavigateToEditClient: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNavigateToEditClient(client.id.toString()) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = client.razaoSocial, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${client.cidade} - ${client.estado}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "Rota: ${client.rota}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
