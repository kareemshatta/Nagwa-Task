package com.kareem.data.models

import com.google.gson.annotations.SerializedName
import com.kareem.domain.entities.FileEntity
import com.kareem.domain.entities.FileState
import com.kareem.domain.mapper.Mapper

data class RemoteFileModel(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("type")
    val type: String,
    @field:SerializedName("url")
    val url: String
) : Mapper<RemoteFileModel, FileEntity> {

    override fun map(from: RemoteFileModel): FileEntity = FileEntity(
        id = id,
        name = name,
        type = type,
        url = url,
        state = FileState.Idle
    )
}