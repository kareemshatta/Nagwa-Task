package com.kareem.domain.useCases

import com.kareem.domain.entities.FileEntity
import com.kareem.domain.repository.FilesRepository
import com.kareem.domain.result.Resource
import io.reactivex.Observable
import javax.inject.Inject

class GetFilesUseCase @Inject constructor(private val repository: FilesRepository) {
    operator fun invoke(): Observable<Resource<List<FileEntity>?>> {
        return Observable.just(
            try {
                val data = repository.getFiles()
                Resource.Success(data)
            } catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        )
    }
}