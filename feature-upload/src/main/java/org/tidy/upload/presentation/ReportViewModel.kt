package org.tidy.upload.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tidy.upload.domain.ReportUseCase
import java.io.File

class ReportViewModel (private val reportUseCase: ReportUseCase) : ViewModel() {
    private val _state = MutableStateFlow<ReportState>(ReportState.Idle)
    val state: StateFlow<ReportState> = _state

    fun extractPdf(file: File) {
        viewModelScope.launch {
            _state.value = ReportState.Loading
            try {
                val response = reportUseCase(file)
                _state.value = ReportState.Success(response.data)
            } catch (e: Exception) {
                _state.value = ReportState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}