package com.anonymous.prohiking.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.UserRepository
import com.anonymous.prohiking.data.utils.Result
import com.anonymous.prohiking.ui.start.LoginViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
): ViewModel() {
    private val currentUserId = preferencesRepository.userId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), -1)

    val currentUser = currentUserId
        .map { id ->
            if (id == -1) {
                null
            } else {
                when (val result = userRepository.getUserById(id)) {
                    is Result.Success -> result.data
                    else -> null
                }
            }
        }
        .onEach { println(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    fun onLogoutButtonPressed() {
        viewModelScope.launch {
            preferencesRepository.updateUserId(-1)
            preferencesRepository.updateUsernameAndPassword("", "")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userRepository = ProHikingApplication.instance.userRepository
                val preferencesRepository = ProHikingApplication.instance.preferencesRepository
                ProfileViewModel(userRepository = userRepository, preferencesRepository = preferencesRepository)
            }
        }
    }
}
