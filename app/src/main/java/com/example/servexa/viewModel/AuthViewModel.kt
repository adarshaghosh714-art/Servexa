package com.example.servexa.viewModel

import android.util.Patterns
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servexa.model.AuthUiState
import com.example.servexa.model.User
import com.example.servexa.repository.AuthRepository
import com.example.servexa.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class AuthViewModel(
    private val repository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()


    // =========================
    // LOGIN
    // =========================

    fun login(
        email: String,
        password: String
    ) {

        _uiState.value = AuthUiState(
            isLoading = true
        )

        viewModelScope.launch {

            val result = repository.login(
                email,
                password
            )

            result.onSuccess {

                _uiState.value = AuthUiState(
                    isLoading = false,
                    isLoggedIn = true
                )
            }

            result.onFailure { exception ->

                _uiState.value = AuthUiState(
                    isLoading = false,
                    isLoggedIn = false,
                    errorMessage = exception.message
                )
            }
        }
    }


    // =========================
    // REGISTER
    // =========================

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        role: String
    ) {

        // -------------------------
        // VALIDATION
        // -------------------------

        if (name.isBlank()) {

            _uiState.value = AuthUiState(
                errorMessage = "Name cannot be empty"
            )

            return
        }

        if (email.isBlank()) {

            _uiState.value = AuthUiState(
                errorMessage = "Email cannot be empty"
            )

            return
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {

            _uiState.value = AuthUiState(
                errorMessage = "Enter a valid email"
            )

            return
        }

        if (password.length < 6) {

            _uiState.value = AuthUiState(
                errorMessage = "Password must contain at least 6 characters"
            )

            return
        }

        if (password != confirmPassword) {

            _uiState.value = AuthUiState(
                errorMessage = "Passwords do not match"
            )

            return
        }
        if (role.isBlank())
            _uiState.value = AuthUiState(
                errorMessage = "Please select an account type"
            )


        // -------------------------
        // FIREBASE REGISTRATION
        // -------------------------

        _uiState.value = AuthUiState(
            isLoading = true
        )

        viewModelScope.launch {

            val result = repository.register(
                name = name,
                email = email,
                password = password,
                role = role
            )

            result.onSuccess {
                _uiState.value = AuthUiState(
                    isLoading = false,
                    isLoggedIn = true
                )
            }

            result.onFailure { exception ->
                _uiState.value = AuthUiState(
                    isLoading = false,
                    isLoggedIn = false,
                    errorMessage = exception.message
                )
            }
        }
    }
}