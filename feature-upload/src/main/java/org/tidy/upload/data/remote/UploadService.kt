package org.tidy.upload.data.remote

import okhttp3.MultipartBody
import org.tidy.upload.data.model.ResponseReport
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @Multipart
    @POST("extract")
    suspend fun extractPdf(
        @Part file: MultipartBody.Part
    ): ResponseReport
}