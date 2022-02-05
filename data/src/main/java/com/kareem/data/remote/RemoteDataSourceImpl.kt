package com.kareem.data.remote

import com.kareem.data.dummy_response.getDummyResponse
import com.kareem.data.models.RemoteFileModel
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(): RemoteDataSourceInterface {
    override fun getFiles(): List<RemoteFileModel> {
        return getDummyResponse()
    }
}