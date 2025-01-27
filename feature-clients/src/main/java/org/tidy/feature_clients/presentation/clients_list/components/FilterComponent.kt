package org.tidy.feature_clients.presentation.clients_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterComponent(
    rota: String,
    cidade: String,
    empresa: String,
    onFilterChange: (String, String, String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = rota,
            onValueChange = { onFilterChange(it, cidade, empresa) },
            label = { Text("Rota") }
        )

        OutlinedTextField(
            value = cidade,
            onValueChange = { onFilterChange(rota, it, empresa) },
            label = { Text("Cidade") }
        )

        OutlinedTextField(
            value = empresa,
            onValueChange = { onFilterChange(rota, cidade, it) },
            label = { Text("Empresa") }
        )
    }
}