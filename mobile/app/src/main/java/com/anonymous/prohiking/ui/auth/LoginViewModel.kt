package com.anonymous.prohiking.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.UserRepository
import com.anonymous.prohiking.data.utils.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object LoggedIn: LoginUiState

    data class LoggedOut(
        val usernameText: String = "",
        val passwordText: String = ""
    ): LoginUiState

    object Loading: LoginUiState
}

class LoginViewModel(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.LoggedOut())
    val uiState = _uiState.asStateFlow()

    init {
//        tryLogin()
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
            uiState.collect { uiState ->
                when (uiState) {
                    is LoginUiState.LoggedOut -> {
                        val username = uiState.usernameText
                        val password = uiState.passwordText

                        _uiState.update { LoginUiState.Loading }
                        if (login(username, password)) {
                            preferencesRepository.setLoggedIn(true)
                            _uiState.update { LoginUiState.LoggedIn }
                        } else {
                            preferencesRepository.setLoggedIn(false)
                            _uiState.update { LoginUiState.LoggedOut() }
                        }
                    }
                    else -> {}
                }
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

            if (login(username, password)) {
                preferencesRepository.setLoggedIn(true)
                _uiState.update { LoginUiState.LoggedIn }
            } else {
                preferencesRepository.setLoggedIn(false)
                _uiState.update { LoginUiState.LoggedOut() }
            }
        }
    }

    private suspend fun login(username: String, password: String): Boolean {
        return when (userRepository.loginUser(username, password)) {
            is ResultWrapper.Success -> true
            is ResultWrapper.Error -> false
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