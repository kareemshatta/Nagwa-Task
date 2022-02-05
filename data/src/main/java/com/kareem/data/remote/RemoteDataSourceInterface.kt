package com.kareem.data.remote

import com.kareem.data.models.RemoteFileModel
import com.kareem.domain.entities.FileEntity
import io.reactivex.Observable

interface RemoteDataSourceInterface {
    fun getFiles(): MutableList<RemoteFileModel>
    fun downloadFile(file: FileEntity): Observable<FileEntity>
}