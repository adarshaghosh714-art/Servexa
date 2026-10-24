package com.example.servexa.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.servexa.model.User
import com.example.servexa.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun getCurrentUser() {

        viewModelScope.launch {

            val currentUser = repository.getCurrentUser()

            _user.value = currentUser
        }
    }
    fun checkLoginStatus(onResult:(User?)-> Unit){
        viewModelScope.launch {
            val currentUser =repository.getCurrentUser()
            onResult(currentUser)
        }
    }

    fun updateUser(
        name: String,
        currentRole: String,
        roles: List<String>
    ) {
        viewModelScope.launch {
            val result = repository.updateProfile(
                name = name,
                currentRole = currentRole,
                roles = roles
            )
            result.onSuccess {
                getCurrentUser()
            }
            result.onFailure { exception ->
                _errorMessage.value = exception.message ?: "Failed to update the profile"
            }
        }
    }
    fun updateServices(services:List<String>){
        viewModelScope.launch{
            val result = repository.updateServices(services)
            result.onSuccess {
                getCurrentUser()
            }
            result.onFailure {
                exception ->
                _errorMessage.value=exception.message?:"Failed to update the services"
            }
        }
    }


    fun switchAccountRole(
        newRole: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _errorMessage.value = null

            val result = repository.switchAccountRole(newRole)

            result.onSuccess {
                val updatedUser = repository.getCurrentUser()
                _user.value = updatedUser

                if (updatedUser?.currentRole == newRole) {
                    onSuccess()
                } else {
                    _errorMessage.value =
                        "Account switched, but refreshing your profile failed."
                }
            }

            result.onFailure { exception ->
                _errorMessage.value =
                    exception.message ?: "Failed to switch account"
            }
        }
    }

}