package com.kareem.nagwa_task.ui.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kareem.domain.entities.FileEntity
import com.kareem.domain.entities.FileState
import com.kareem.domain.entities.FileType
import com.kareem.nagwa_task.R
import com.kareem.nagwa_task.databinding.AttachmentLayoutBinding

class FilesAdapter(private val listener: FileClickListener) :
    RecyclerView.Adapter<FilesAdapter.FileViewHolder>() {
    private var fileList: MutableList<FileEntity> = mutableListOf()

    inner class FileViewHolder(val binding: AttachmentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivDownloadLogo.setOnClickListener {
                listener.onDownloadFileClick(fileList[adapterPosition])
                binding.ivDownloadLogo.isEnabled = false
            }
        }

        fun bind(file: FileEntity) {
            binding.ivAttachmentLogo.setImageResource(
                when (file.type) {
                    FileType.VIDEO -> R.drawable.ic_video
                    FileType.PDF -> R.drawable.ic_pdf
                }
            )
            binding.tvAttachmentName.text = file.name
            handleFileState(this, file.state)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(
            AttachmentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(fileList[position])
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FileViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        val item = fileList[holder.adapterPosition]
        handleFileState(holder, item.state)

        if (payloads.firstOrNull() != null) {
            with(holder.binding) {
                val bundle = payloads.first() as Bundle
                val progress = bundle.getInt("progressValue")
                if (progress > -1) {
                    pbDownloadFile.progress = progress
                    tvDownloadPercentage.text = "$progress %"
                } else {
                    pbDownloadFile.isIndeterminate = progress <= -1

                }
            }
        }
    }

    override fun getItemCount() = fileList.size

    @JvmName("setFileList1")
    @SuppressLint("NotifyDataSetChanged")
    fun setFileList(list: MutableList<FileEntity>) {
        this.fileList = list
        notifyDataSetChanged()
    }

    private fun handleFileState(holder: FileViewHolder, state: FileState) {
        when (state) {
            is FileState.Idle -> bindFileIdle(holder)
            is FileState.DownLoading -> bindDownloadingFile(holder, state.progress)
            is FileState.DownloadSuccess -> bindDownloadedFile(holder)
            is FileState.DownloadFailure -> bindFailureFile(holder)
        }

    }

    private fun bindFileIdle(holder: FileViewHolder) {
        holder.binding.ivDownloadLogo.setImageResource(R.drawable.ic_download)
        holder.binding.ivDownloadLogo.isVisible = true
        holder.binding.downloadProgressLayout.isVisible = false
    }

    private fun bindDownloadedFile(holder: FileViewHolder) {
        holder.binding.ivDownloadLogo.setImageResource(R.drawable.ic_done)
        holder.binding.ivDownloadLogo.isVisible = true
        holder.binding.downloadProgressLayout.isVisible = false
    }

    private fun bindDownloadingFile(holder: FileViewHolder, progress: Int) {
        holder.binding.ivDownloadLogo.isVisible = false
        holder.binding.downloadProgressLayout.isVisible = true
    }

    private fun bindFailureFile(holder: FileViewHolder) {
        holder.binding.ivDownloadLogo.setImageResource(R.drawable.ic_retry)
        holder.binding.ivDownloadLogo.isVisible = true
        holder.binding.downloadProgressLayout.isVisible = false
    }

    fun updateFile(fileEntity: FileEntity) {
        fileList.find { fileEntity.id == it.id }?.let {

            val fileIndex = fileList.indexOf(it)
            fileList[fileIndex] = fileEntity

            if (fileEntity.state is FileState.DownLoading) {
                notifyItemChanged(fileIndex, Bundle().apply {
                    putInt("progressValue", (fileEntity.state as FileState.DownLoading).progress)
                })
            } else {
                notifyItemChanged(fileIndex)
            }
        }
    }
}

interface FileClickListener {
    fun onDownloadFileClick(file: FileEntity)
}