package com.kareem.domain.repository

import com.kareem.domain.entities.FileEntity
import io.reactivex.Observable

interface FilesRepository {

    fun getFiles(): MutableList<FileEntity>
    fun downloadFile(fileEntity: FileEntity) : Observable<FileEntity>
}