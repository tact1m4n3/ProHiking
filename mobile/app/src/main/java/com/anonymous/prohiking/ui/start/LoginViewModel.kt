package com.anonymous.prohiking.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.UserRepository
import com.anonymous.prohiking.data.network.utils.ApiResult
import com.anonymous.prohiking.data.network.utils.ErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Loading: LoginUiState

    data class LoggedOut(
        val usernameText: String = "",
        val passwordText: String = "",
        val errorMessage: String = "",
    ): LoginUiState

    object LoggedIn: LoginUiState
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

    private fun tryLogin() {
        viewModelScope.launch {
            val userId = preferencesRepository.userId.first()
            if (userId == -1) {
                _uiState.update { LoginUiState.LoggedOut() }
                return@launch
            }

            val username = preferencesRepository.username.first()
            val password = preferencesRepository.password.first()

            when (val result = userRepository.loginUser(username, password)) {
                is ApiResult.Success -> {
                    preferencesRepository.updateUserId(result.data.id)
                    _uiState.update { LoginUiState.LoggedIn }
                }
                is ApiResult.Error -> {
                    when (result.error) {
                        is ErrorType.Network -> _uiState.update { LoginUiState.LoggedIn }
                        else -> {
                            preferencesRepository.updateUserId(-1)
                            preferencesRepository.updateUsernameAndPassword("", "")
                            _uiState.update { LoginUiState.LoggedOut(errorMessage = "Failed to login") }
                        }
                    }
                }
            }
        }
    }

    fun onLoginButtonClick() {
        viewModelScope.launch {
            when (val currentState = _uiState.getAndUpdate { LoginUiState.Loading }) {
                is LoginUiState.LoggedOut -> {
                    val username = currentState.usernameText
                    val password = currentState.passwordText

                    when (val result = userRepository.loginUser(username, password)) {
                        is ApiResult.Success -> {
                            preferencesRepository.updateUserId(result.data.id)
                            preferencesRepository.updateUsernameAndPassword(username, password)
                            _uiState.update { LoginUiState.LoggedIn }
                        }
                        is ApiResult.Error -> {
                            preferencesRepository.updateUserId(-1)
                            preferencesRepository.updateUsernameAndPassword("", "")
                            _uiState.update { LoginUiState.LoggedOut(errorMessage = "Failed to login") }
                        }
                    }
                }
                else -> {}
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