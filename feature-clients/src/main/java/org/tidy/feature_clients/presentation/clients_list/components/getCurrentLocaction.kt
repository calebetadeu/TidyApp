package org.tidy.feature_clients.presentation.clients_list.components

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import org.tidy.feature_clients.presentation.edit_client.EditClientAction
import org.tidy.feature_clients.presentation.edit_client.EditClientViewModel
import org.tidy.feature_clients.presentation.register_client.RegisterClientAction
import org.tidy.feature_clients.presentation.register_client.RegisterClientViewModel


@SuppressLint("MissingPermission")
fun getCurrentLocation(
    viewModel: Any, // Permite receber diferentes ViewModels
    context: Context
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            when (viewModel) {
                is EditClientViewModel -> {
                    viewModel.onAction(EditClientAction.OnLocalizacaoChange("${it.latitude}, ${it.longitude}"))
                }
                is RegisterClientViewModel -> {
                    viewModel.onAction(RegisterClientAction.OnLocalizacaoChange("${it.latitude}, ${it.longitude}"))
                }
                else -> throw IllegalArgumentException("ViewModel não suportado")
            }
        }
    }
}