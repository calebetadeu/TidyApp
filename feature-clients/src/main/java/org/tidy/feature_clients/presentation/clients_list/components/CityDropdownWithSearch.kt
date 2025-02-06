package org.tidy.feature_clients.presentation.clients_list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import org.tidy.feature_clients.data.remote.LocationDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdownWithSearch(
    locations: List<LocationDto>,
    selectedState: String,
    selectedCity: String,
    defaultCity: String, // Valor padrão para a cidade, caso selectedCity esteja vazio
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    // Inicializa currentCity com selectedCity; se estiver vazio, utiliza defaultCity
    var currentCity by remember { mutableStateOf(if (selectedCity.isEmpty()) defaultCity else selectedCity) }

    // Busca a lista de cidades correspondente ao estado selecionado
    val cities = locations.find { it.estado == selectedState }?.listaCidades ?: emptyList()
    val filteredCities = if (searchQuery.isNotEmpty()) {
        cities.filter { it.contains(searchQuery, ignoreCase = true) }
    } else {
        cities
    }

    // Se selectedCity estiver vazio, define o defaultCity como cidade selecionada
    LaunchedEffect(defaultCity) {
        if (selectedCity.isEmpty() && defaultCity.isNotEmpty()) {
            currentCity = defaultCity
            onCitySelected(defaultCity)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // Campo que exibe a cidade selecionada
        OutlinedTextField(
            value = currentCity,
            onValueChange = { },
            label = { Text("Cidade") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            modifier = Modifier
                .menuAnchor() // Necessário para o correto funcionamento do dropdown
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Campo de busca dentro do dropdown
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Pesquisar cidade") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            // Lista de cidades filtradas
            filteredCities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        currentCity = city
                        onCitySelected(city)
                        expanded = false
                        searchQuery = ""
                    }
                )
            }
        }
    }
}