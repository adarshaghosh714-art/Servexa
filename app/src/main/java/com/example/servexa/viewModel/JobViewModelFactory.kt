package com.example.servexa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servexa.repository.JobRepository

class JobViewModelFactory(
    private val repository: JobRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(JobViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return JobViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}