package com.anonymous.prohiking.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.UserRepository
import com.anonymous.prohiking.data.UserRepositoryImpl
import com.anonymous.prohiking.data.utils.ErrorType
import com.anonymous.prohiking.data.utils.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ExploreViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false )
    val isSearching = _isSearching.asStateFlow()

    private val _tracks = MutableStateFlow(allTracks)
    val tracks = searchText
        .debounce(1000L)
        .onEach { _isSearching.update {true} }
        .combine(_tracks ) { text, tracks ->
            if(text.isBlank()) {
                tracks
            } else{
                tracks.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update{false} }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _tracks.value
        )

    init {
        getUser()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text.trimEnd('\n')
    }

    private fun getUser() {
        viewModelScope.launch {
            viewModelScope.launch {
                when (val result = userRepository.loginUser("testuser", "testpass123")) {
                    is ResultWrapper.Success -> {
                        println(result.data)
                    }
                    is ResultWrapper.Error -> {
                        println("failed to login")
                    }
                }
            }.join()

            when (val result = userRepository.getUserById(1)) {
                is ResultWrapper.Success -> {
                    println(result.data)
                }
                is ResultWrapper.Error -> {
                    println("failed to fetch user")
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = ProHikingApplication.instance
                val userRepository = application.container.userRepository
                ExploreViewModel(userRepository = userRepository)
            }
        }
    }
}

data class Track(
    val name: String
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            "${name.first()}"
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

private val allTracks = listOf(
    Track (
        name = "Cabana Omu"
    ),
    Track (
        name = "Cabana Diham"
    ),
    Track (
        name = "Moroeni - Plaiul Proporului - Cabana Scropoasa"
    ),
    Track (
        name = "Sinaia- Bucegi- Platoul Masivului Bucegi - Sinaia"
    )
)