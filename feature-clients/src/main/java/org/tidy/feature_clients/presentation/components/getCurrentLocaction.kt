package org.tidy.feature_clients.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import org.tidy.feature_clients.domain.model.Localization
import org.tidy.feature_clients.presentation.edit_client.EditClientAction
import org.tidy.feature_clients.presentation.edit_client.EditClientViewModel
import org.tidy.feature_clients.presentation.register_list.RegisterClientAction
import org.tidy.feature_clients.presentation.register_list.RegisterClientViewModel


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
                    val localization: Localization = Localization(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                    viewModel.onAction(EditClientAction.OnLocalizacaoChange(localization))
                }
                is RegisterClientViewModel -> {
                    viewModel.onAction(RegisterClientAction.OnLocalizacaoChange("${it.latitude}, ${it.longitude}"))
                }
                else -> throw IllegalArgumentException("ViewModel não suportado")
            }
        }
    }
}