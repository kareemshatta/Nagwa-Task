package com.kareem.domain.repository

import com.kareem.domain.entities.FileEntity

interface FilesRepository {

    fun getFiles(): List<FileEntity>

}