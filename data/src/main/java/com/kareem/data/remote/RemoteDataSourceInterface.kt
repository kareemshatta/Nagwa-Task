package com.kareem.data.remote

import com.kareem.data.models.RemoteFileModel

interface RemoteDataSourceInterface {
    fun getFiles(): List<RemoteFileModel>
}