package com.kareem.nagwa_task.di.modules

import com.kareem.data.remote.RemoteDataSourceImpl
import com.kareem.data.remote.RemoteDataSourceInterface
import com.kareem.data.repositoryImp.FileRepositoryImp
import com.kareem.domain.repository.FilesRepository
import dagger.Module

@Module
interface DataModule {

    @dagger.Binds
    fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSourceInterface

    @dagger.Binds
    fun bindFilesRepository(
        repo: FileRepositoryImp
    ): FilesRepository

}