package com.kareem.data.remote

import com.kareem.data.dummy_response.getDummyResponse
import com.kareem.data.models.RemoteFileModel
import com.kareem.domain.entities.FileEntity
import com.kareem.domain.entities.FileState
import io.reactivex.Observable
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val fakeDownloadFileWithProgress: FakeDownloadFileWithProgress
) : RemoteDataSourceInterface {
    override fun getFiles(): MutableList<RemoteFileModel> {
        return getDummyResponse()
    }

    override fun downloadFile(file: FileEntity): Observable<FileEntity> {
        return Observable.create { emitter ->
            val progressListener = object : DownloadProgressListener {
                override fun updateDownloadingProgress(
                    bytesRead: Long,
                    contentLength: Long,
                    done: Boolean
                ) {
                    if (done) {
                        file.state = FileState.DownloadSuccess
                        emitter.onNext(file)
                        emitter.onComplete()
                    } else {
                        val progress = bytesRead * 100 / contentLength
                        file.state = FileState.DownLoading(progress = progress.toInt())
                        emitter.onNext(file)
                    }

                }

                override fun downloadFailed() {
                    file.state = FileState.DownloadFailure
                    emitter.onNext(file)
                }
            }
            fakeDownloadFileWithProgress.downloadProgressListener = progressListener
            fakeDownloadFileWithProgress.invoke(file.url)
        }
    }
}