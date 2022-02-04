package com.kareem.data.repositoryImp

import com.kareem.data.remote.RemoteDataSourceInterface
import com.kareem.domain.entities.FileEntity
import com.kareem.domain.repository.FilesRepository
import javax.inject.Inject

class FileRepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSourceInterface
) : FilesRepository {
    override fun getFiles(): List<FileEntity> {
        return remoteDataSource.getFiles().map { it.map(it) }
    }
}