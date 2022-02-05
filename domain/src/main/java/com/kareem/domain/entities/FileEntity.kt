package com.kareem.domain.entities


data class FileEntity(
    val id: Int,
    val name: String,
    val type: FileType,
    val url: String,
    var state: FileState = FileState.Idle
)

enum class FileType{
    VIDEO, PDF
}

sealed class FileState {
    object Idle : FileState()
    class DownLoading(progress: Int) : FileState()
    object DownloadSuccess : FileState()
    class DownloadFailure(msg: String) : FileState()
}