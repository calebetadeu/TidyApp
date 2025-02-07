package org.tidy.upload.presentation

import org.tidy.upload.data.model.Report

// Estados possíveis da UI
sealed class ReportState {
    object Idle : ReportState()
    object Loading : ReportState()
    data class Success(val data: List<Report>) : ReportState()
    data class Error(val message: String) : ReportState()
}
