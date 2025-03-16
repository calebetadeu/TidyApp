package org.tidy.tidyapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tidy.core_ui.theme.primaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userEmail: String?,
    onNavigateToClients: () -> Unit,
    onNavigateToBilling: () -> Unit,
    onNavigateToUpdates: () -> Unit,
    onNavigateToPlanning: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bem-vindo, $userEmail") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = primaryLight)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QuickAccessButton("Clientes", onNavigateToClients)
            QuickAccessButton("Faturamento", onNavigateToBilling, enable = false)
            QuickAccessButton("Atualizações", onNavigateToUpdates, enable = true)
            QuickAccessButton("Programações",  onNavigateToPlanning, enable = false)
        }
    }
}

@Composable
fun QuickAccessButton(text: String, onClick: () -> Unit,enable: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enable,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text)
        }
    }
}