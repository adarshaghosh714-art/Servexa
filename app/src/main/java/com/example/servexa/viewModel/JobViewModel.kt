package com.example.servexa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.servexa.model.Job
import com.example.servexa.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JobViewModel(private val repository: JobRepository) : ViewModel() {
    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs = _jobs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun getJobs() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getJobs()
            result.onSuccess { jobList ->
                _jobs.value = jobList
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Failed to load the jobs"
                _isLoading.value = false
            }
        }
    }

    fun addJob(job: Job) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.addJobs(job)

            result.onSuccess {
                _isLoading.value = false

            }
            result.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Failed to add the job"
                _isLoading.value = false
            }
        }
    }

    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            val result = repository.deleteJob(jobId)
            result.onSuccess {
                getJobs()
            }
            result.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Failed to delete the jobs"
            }
        }
    }
    fun getCustomerJobs() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getCustomerJobs()

            result.onSuccess { jobList ->
                _jobs.value = jobList
                _isLoading.value = false
            }

            result.onFailure { exception ->
                _errorMessage.value =
                    exception.message ?: "Failed to load customer jobs"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

}