package com.kareem.domain.entities


data class FileEntity(
    val id: Int,
    val name: String,
    val type: String,
    val url: String,
    var state: FileState = FileState.Idle
)

sealed class FileState {
    object Idle : FileState()
    class DownLoading(progress: Int) : FileState()
    object DownloadSuccess : FileState()
    class DownloadFailure(msg: String) : FileState()

}