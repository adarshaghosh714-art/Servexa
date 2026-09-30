package com.example.servexa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servexa.model.Provider
import com.example.servexa.repository.ProviderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProviderViewModel (private val repository: ProviderRepository): ViewModel(){
    private val _providers = MutableStateFlow<List<Provider>>(emptyList())

    val providers = _providers.asStateFlow()
    private val _isLoading= MutableStateFlow(false)
     val isLoading= _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun getProviderByService(service: String){
        viewModelScope.launch {
            _isLoading.value=true
            _errorMessage.value=null

            val result = repository.getProviderByServices(service)
            result.onSuccess { providerList->
                _providers.value=providerList
                _isLoading.value=false
            }
            result.onFailure{exception ->
                _errorMessage.value=exception.message?:"Failed to load providers"
                _isLoading.value=false
            }
        }
    }
    fun clearProviders(){
        _providers.value=emptyList()
    }
    fun clearError(){
        _errorMessage.value = null
    }

}