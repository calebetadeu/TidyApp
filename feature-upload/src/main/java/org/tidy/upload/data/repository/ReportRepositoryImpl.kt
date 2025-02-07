package org.tidy.upload.data.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tidy.upload.data.model.ResponseReport
import org.tidy.upload.data.remote.UploadService
import org.tidy.upload.domain.ReportRepository
import java.io.File

class ReportRepositoryImpl (private val uploadService: UploadService): ReportRepository {
    override suspend fun extractPdf(file: File): ResponseReport {
        val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return uploadService.extractPdf(body)
    }

}