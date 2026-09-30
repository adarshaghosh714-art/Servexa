package com.example.servexa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servexa.repository.AuthRepository
import com.example.servexa.repository.UserRepository

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(
                repository,
                userRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}