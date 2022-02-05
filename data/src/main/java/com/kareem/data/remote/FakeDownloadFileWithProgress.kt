package com.kareem.data.remote

import com.kareem.data.models.DownloadResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class FakeDownloadFileWithProgress @Inject constructor() {

    lateinit var downloadProgressListener: DownloadProgressListener

    @Throws(IOException::class)
    operator fun invoke(fileUrl: String) {
        try {
            val request = Request.Builder()
                .url(fileUrl)
                .build()
            val client = OkHttpClient.Builder()
                .addNetworkInterceptor { chain: Interceptor.Chain ->
                    val response = chain.proceed(chain.request())
                    response.newBuilder()
                        .body(DownloadResponseBody(response.body!!, downloadProgressListener))
                        .build()
                }.build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful)
                throw IOException(response.message)
            else
//                download file
            response.body?.string()

        } catch (e: Exception) {
            downloadProgressListener.downloadFailed()
        }
    }

}

interface DownloadProgressListener {
    fun updateDownloadingProgress(bytesRead: Long, contentLength: Long, done: Boolean)
    fun downloadFailed()
}