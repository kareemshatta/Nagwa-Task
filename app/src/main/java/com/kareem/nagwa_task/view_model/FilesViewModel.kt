package com.kareem.nagwa_task.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kareem.domain.entities.FileEntity
import com.kareem.domain.entities.FileState
import com.kareem.domain.result.Resource
import com.kareem.domain.useCases.DownloadFilesUseCase
import com.kareem.domain.useCases.GetFilesUseCase
import com.kareem.nagwa_task.di.qualifiers.IoScheduler
import com.kareem.nagwa_task.di.qualifiers.MainScheduler
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FilesViewModel @Inject constructor(
    private val getFilesUseCase: GetFilesUseCase,
    private val downloadFilesUseCase: DownloadFilesUseCase,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainScheduler: Scheduler
) : ViewModel() {

    val viewState by lazy {
        MutableLiveData<ScreenState>()
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun getFiles() {
        compositeDisposable.add(
            getFilesUseCase.invoke()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({
                    when (it) {
                        is Resource.Success -> viewState.postValue(
                            ScreenState.GetFilesIsSuccess(it.data!!)
                        )
                        is Resource.Loading -> viewState.postValue(ScreenState.GetFilesIsLoading)
                        is Resource.Error -> viewState.postValue(
                            ScreenState.GetFilesIsFailure(
                                it.message ?: "unexpected error"
                            )
                        )
                    }
                }, {
                    viewState.postValue(
                        ScreenState.GetFilesIsFailure(
                            it.message ?: "unexpected error"
                        )
                    )
                })
        )
    }

    fun downloadFile(fileEntity: FileEntity) {
        compositeDisposable.add(
            downloadFilesUseCase.invoke(fileEntity)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({
                    when (it.state) {
                        is FileState.DownLoading -> {
                            viewState.postValue(
                                ScreenState.DownloadFileIsLoading(it)
                            )
                        }
                        is FileState.DownloadFailure -> {
                            viewState.postValue(
                                ScreenState.DownloadFileIsFailed(it)
                            )
                        }
                        FileState.DownloadSuccess -> {
                            viewState.postValue(
                                ScreenState.FileIsDownloaded(it)
                            )
                        }
                    }
                }, {
                    fileEntity.state = FileState.DownloadFailure
                    viewState.postValue(ScreenState.DownloadFileIsFailed(fileEntity))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

sealed class ScreenState {
    object GetFilesIsLoading : ScreenState()
    class GetFilesIsSuccess(val data: MutableList<FileEntity>) : ScreenState()
    class GetFilesIsFailure(val msg: String) : ScreenState()

    class DownloadFileIsLoading(val file: FileEntity) : ScreenState()
    class FileIsDownloaded(val file: FileEntity) : ScreenState()
    class DownloadFileIsFailed(val file: FileEntity) : ScreenState()
}