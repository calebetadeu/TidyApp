package org.tidy.feature_clients.presentation.clients_list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.tidy.feature_clients.data.remote.LocationDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdown(
    locations: List<LocationDto>,
    defaultState: String,
    selectedState: String,
    onStateSelected: (String) -> Unit
) {
    // Obtém a lista de estados, removendo duplicatas e ordenando
    val states = locations.map { it.estado }.distinct().sorted()

    // Se não houver estado selecionado e houver estados disponíveis, define o primeiro como padrão
    val defaultState = if (selectedState.isEmpty() && states.isNotEmpty()) defaultState else selectedState

    var expanded by remember { mutableStateOf(false) }

    // Se o estado estiver vazio, automaticamente define o valor padrão via callback
    LaunchedEffect(defaultState) {
        if (selectedState.isEmpty() && states.isNotEmpty()) {
            onStateSelected(defaultState)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = defaultState,
            onValueChange = { },
            label = { Text("Estado") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            states.forEach { stateItem ->
                DropdownMenuItem(
                    text = { Text(stateItem) },
                    onClick = {
                        onStateSelected(stateItem)
                        expanded = false
                    }
                )
            }
        }
    }
}