package com.kareem.nagwa_task.ui.adapter

import android.annotation.SuppressLint
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
    private var fileList: List<FileEntity> = emptyList()

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
            AttachmentLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(fileList[position])
    }

    override fun getItemCount() = fileList.size

    @JvmName("setFileList1")
    @SuppressLint("NotifyDataSetChanged")
    fun setFileList(list: List<FileEntity>) {
        this.fileList = list
        notifyDataSetChanged()
    }

    private fun handleFileState(holder: FileViewHolder, state: FileState) {
        when (state) {
            is FileState.Idle -> bindFileIdle(holder)
            is FileState.DownLoading -> bindDownloadingFile(holder)
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

    private fun bindDownloadingFile(holder: FileViewHolder) {
        holder.binding.ivDownloadLogo.isVisible = false
        holder.binding.downloadProgressLayout.isVisible = true
    }

    private fun bindFailureFile(holder: FileViewHolder) {
        holder.binding.ivDownloadLogo.setImageResource(R.drawable.ic_retry)
        holder.binding.ivDownloadLogo.isVisible = true
        holder.binding.downloadProgressLayout.isVisible = false
    }
}

interface FileClickListener {
    fun onDownloadFileClick(file: FileEntity)
}

//override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
//    super.onBindViewHolder(holder, position, payloads)
//
//    var item = items.get(holder.adapterPosition)
//    handleFileState(holder,item.state)
//
//    if (payloads.firstOrNull() != null) {
//        with(holder.itemView) {
//
//            var bundle = payloads.first() as Bundle
//            val progress = bundle.getInt("progress")
//            if (progress != -1) {
//                pro_downlaod.progress = progress
//                tv_percentage.text = "$progress%"
//            } else {
//                pro_downlaod.isIndeterminate = progress == -1
//
//            }
//        }
//    }
//fun setProgress(fileEntity: FileEntity) {
//    var model = getFile(fileEntity)
//
//    notifyItemChanged(items.indexOf(model), Bundle().apply {
//        putInt("progress", fileEntity.progress)
//    })
//}
//


//}


//private fun getFile(fileEntity: FileEntity) = items.find { fileEntity.id == it.id }
