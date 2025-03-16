package org.tidy.upload.domain

import org.tidy.upload.data.model.ResponseReport
import java.io.File

class ReportUseCase(private val repository: ReportRepository) {
    suspend operator fun invoke(file: File): ResponseReport {
        return repository.extractPdf(file)
    }
}