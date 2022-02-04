package com.kareem.nagwa_task.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kareem.domain.entities.FileEntity
import com.kareem.domain.result.Resource
import com.kareem.domain.useCases.GetFilesUseCase
import com.kareem.nagwa_task.di.qualifiers.IoScheduler
import com.kareem.nagwa_task.di.qualifiers.MainScheduler
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FilesViewModel @Inject constructor(
    private val getFilesUseCase: GetFilesUseCase,
    @IoScheduler private val ioScheduler: Scheduler,
    @MainScheduler private val mainScheduler: Scheduler
) : ViewModel() {

    val viewState =
        MutableLiveData<Resource<List<FileEntity>?>>(Resource.Idle())

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun getFiles() {
        compositeDisposable.add(
            getFilesUseCase.invoke()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({
                    viewState.postValue(it)
                }, {
                    viewState.postValue(Resource.Error(it.localizedMessage?: ""))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}