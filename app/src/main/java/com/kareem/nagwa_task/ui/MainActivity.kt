package com.kareem.nagwa_task.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kareem.domain.entities.FileEntity
import com.kareem.domain.entities.FileState
import com.kareem.domain.result.Resource
import com.kareem.nagwa_task.databinding.ActivityMainBinding
import com.kareem.nagwa_task.ui.adapter.FileClickListener
import com.kareem.nagwa_task.ui.adapter.FilesAdapter
import com.kareem.nagwa_task.view_model.FilesViewModel
import com.kareem.nagwa_task.view_model.ScreenState
import com.kareem.nagwa_task.view_model.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FileClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        FilesAdapter(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<FilesViewModel>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(FilesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        viewModel.getFiles()
        observeOnViewState()
    }

    private fun observeOnViewState() {
        viewModel.viewState.observe(this, Observer {
            onViewStateChanged(it)
        })
    }

    private fun onViewStateChanged(state: ScreenState) {
        when (state) {
            is ScreenState.DownloadFileIsFailed -> updateFileState(state.file)
            is ScreenState.DownloadFileIsLoading -> updateFileState(state.file)
            is ScreenState.FileIsDownloaded -> updateFileState(state.file)
            is ScreenState.GetFilesIsFailure -> handleViewWhenStateFailed(state.msg)
            is ScreenState.GetFilesIsLoading -> binding.progressBar.isVisible = true
            is ScreenState.GetFilesIsSuccess -> handleViewWhenStateSuccess(state)
        }
    }

    private fun updateFileState(file: FileEntity) {
        adapter.updateFile(file)
    }

    private fun handleViewWhenStateFailed(message: String?) {
        binding.progressBar.isVisible = false
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleViewWhenStateSuccess(state: ScreenState.GetFilesIsSuccess) {
        binding.progressBar.isVisible = false
        adapter.setFileList(state.data)
    }

    private fun setupRecyclerView() {
        binding.rvAttachmentsList.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDownloadFileClick(file: FileEntity) {
        if (file.state is FileState.Idle || file.state is FileState.DownloadFailure) {
            viewModel.downloadFile(file)
        }
    }
}