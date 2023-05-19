package com.anonymous.prohiking.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.UserRepository
import com.anonymous.prohiking.data.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface RegisterUiState {
    object Registered: RegisterUiState

    data class NotRegistered(
        val usernameText: String = "",
        val emailText: String = "",
        val passwordText: String = "",
        val verifyPasswordText: String = "",
        val errorMessage: String = "",
    ): RegisterUiState

    object Loading: RegisterUiState
}

class RegisterViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.NotRegistered())
    val uiState = _uiState.asStateFlow()

    fun updateUsernameText(text: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is RegisterUiState.NotRegistered -> currentState.copy(usernameText = text)
                else -> currentState
            }
        }
    }

    fun updateEmailText(text: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is RegisterUiState.NotRegistered -> currentState.copy(emailText = text)
                else -> currentState
            }
        }
    }

    fun updatePasswordText(text: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is RegisterUiState.NotRegistered -> currentState.copy(passwordText = text)
                else -> currentState
            }
        }
    }

    fun updateVerifyPasswordText(text: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is RegisterUiState.NotRegistered -> currentState.copy(verifyPasswordText = text)
                else -> currentState
            }
        }
    }

    fun onRegisterButtonClick() {
        viewModelScope.launch {
            when (val currentState = _uiState.getAndUpdate { RegisterUiState.Loading }) {
                is RegisterUiState.NotRegistered -> {
                    val username = currentState.usernameText
                    val email = currentState.emailText
                    val password = currentState.passwordText
                    val verifyPassword = currentState.verifyPasswordText

                    if (password == verifyPassword) {
                        when (val result = userRepository.registerUser(username, email, password)) {
                            is Result.Success -> {
                                _uiState.update { RegisterUiState.Registered }
                            }
                            is Result.Error -> {
                                println(result)
                                _uiState.update { RegisterUiState.NotRegistered(errorMessage = "Failed to register") }
                            }
                        }
                    } else {
                        _uiState.update { RegisterUiState.NotRegistered(errorMessage = "Passwords do not match") }
                    }
                }
                else -> {}
            }
        }
    }

    fun reset() {
        _uiState.value = RegisterUiState.NotRegistered()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userRepository = ProHikingApplication.instance.userRepository
                RegisterViewModel(userRepository = userRepository)
            }
        }
    }
}