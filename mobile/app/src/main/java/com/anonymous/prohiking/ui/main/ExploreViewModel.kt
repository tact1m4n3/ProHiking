package com.anonymous.prohiking.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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

    fun onSearchTextChange(text: String) {
        _searchText.value = text.trimEnd('\n')
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userRepository = ProHikingApplication.instance.userRepository
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