package org.tidy.feature_clients.presentation.edit_client


import EditClientViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClientScreen(
    clientId: Int,
    viewModel: EditClientViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // 🚀 **Solicitação de Permissão de Localização**
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(viewModel, context)
        }
    }

    // 🚀 **Carregar o cliente ao abrir a tela**
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
                        Icon(
                            Icons.Default.CloudSync,
                            contentDescription = "Sincronizar com Firebase"
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
            ) {
                EditTextField(label = "Razão Social", value = state.razaoSocial) {
                    viewModel.onAction(EditClientAction.OnRazaoSocialChange(it))
                }

                EditTextField(label = "CNPJ", value = state.cnpj ?: "") {
                    viewModel.onAction(EditClientAction.OnCnpjChange(it))
                }

                EditTextField(label = "Latitude", value = "${state.latitude ?: ""}") {
                    viewModel.onAction(EditClientAction.OnLatitudeChange(it.toDoubleOrNull()))
                }

                EditTextField(label = "Longitude", value = "${state.longitude ?: ""}") {
                    viewModel.onAction(EditClientAction.OnLongitudeChange(it.toDoubleOrNull()))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Text("Usar Localização Atual")
                }

                Spacer(modifier = Modifier.height(16.dp))

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

// 🚀 **Função para buscar localização**
@SuppressLint("MissingPermission")
fun getCurrentLocation(viewModel: EditClientViewModel, context: android.content.Context) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            viewModel.onAction(EditClientAction.OnLatitudeChange(it.latitude))
            viewModel.onAction(EditClientAction.OnLongitudeChange(it.longitude))
        }
    }
}

@Preview
@Composable
fun EditClientScreenPreview() {
    EditClientScreen(clientId = 123, onNavigateBack = {})
}