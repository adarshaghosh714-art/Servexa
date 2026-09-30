package com.example.servexa.viewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servexa.repository.ProviderRepository

class ProviderViewModelFactory(
    private val repository: ProviderRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ProviderViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ProviderViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}