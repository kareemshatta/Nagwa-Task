package com.kareem.domain.useCases

import com.kareem.domain.entities.FileEntity
import com.kareem.domain.repository.FilesRepository
import com.kareem.domain.result.Resource
import io.reactivex.Observable
import javax.inject.Inject

class DownloadFilesUseCase @Inject constructor(private val repository: FilesRepository) {
    operator fun invoke(fileEntity: FileEntity) = repository.downloadFile(fileEntity)
}