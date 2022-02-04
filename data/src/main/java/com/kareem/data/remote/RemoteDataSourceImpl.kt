package com.kareem.data.remote

import com.kareem.data.dummy_response.getDummyResponse
import com.kareem.data.models.RemoteFileModel

class RemoteDataSourceImpl: RemoteDataSourceInterface {
    override fun getFiles(): List<RemoteFileModel> {
        return getDummyResponse()
    }
}