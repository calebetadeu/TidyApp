package org.tidy.upload.domain

import org.tidy.upload.data.model.ResponseReport
import java.io.File

interface ReportRepository {
    suspend fun extractPdf(file: File): ResponseReport
}