package com.anonymous.prohiking.ui.auth

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

sealed interface LoginUiState {
    object LoggedIn: LoginUiState

    data class LoggedOut(
        val usernameText: String = "",
        val passwordText: String = "",
        val errorMessage: String = "",
    ): LoginUiState

    object Loading: LoginUiState
}

class LoginViewModel(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        tryLogin()
    }

    fun updateUsernameText(text: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is LoginUiState.LoggedOut -> currentState.copy(usernameText = text)
                else -> currentState
            }
        }
    }

    fun updatePasswordText(text: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is LoginUiState.LoggedOut -> currentState.copy(passwordText = text)
                else -> currentState
            }
        }
    }

    fun onLoginButtonClick() {
        viewModelScope.launch {
            when (val currentState = _uiState.getAndUpdate { LoginUiState.Loading }) {
                is LoginUiState.LoggedOut -> {
                    val username = currentState.usernameText
                    val password = currentState.passwordText

                    when (userRepository.loginUser(username, password)) {
                        is Result.Success -> {
                            preferencesRepository.updateLoggedIn(true)
                            preferencesRepository.updateUsernameAndPassword(username, password)
                            _uiState.update { LoginUiState.LoggedIn }
                        }
                        is Result.Error -> {
                            preferencesRepository.updateLoggedIn(false)
                            preferencesRepository.updateUsernameAndPassword("", "")
                            _uiState.update { LoginUiState.LoggedOut(errorMessage = "Failed to login") }
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun tryLogin() {
        viewModelScope.launch {
            val loggedIn = preferencesRepository.loggedIn.first()
            if (!loggedIn) {
                _uiState.update { LoginUiState.LoggedOut() }
                return@launch
            }

            val username = preferencesRepository.username.first()
            val password = preferencesRepository.password.first()

            when (userRepository.loginUser(username, password)) {
                is Result.Success -> {
                    preferencesRepository.updateLoggedIn(true)
                    _uiState.update { LoginUiState.LoggedIn }
                }
                is Result.Error -> {
                    preferencesRepository.updateLoggedIn(false)
                    preferencesRepository.updateUsernameAndPassword("", "")
                    _uiState.update { LoginUiState.LoggedOut(errorMessage = "Failed to login") }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userRepository = ProHikingApplication.instance.userRepository
                val preferencesRepository = ProHikingApplication.instance.preferencesRepository
                LoginViewModel(userRepository = userRepository, preferencesRepository = preferencesRepository)
            }
        }
    }
}