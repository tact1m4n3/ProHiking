package com.anonymous.prohiking.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.prohiking.data.remote.LoginPayload
import com.anonymous.prohiking.data.remote.ProHikingApi
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

class ExploreViewModel: ViewModel() {
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
        loginUser()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text.trimEnd('\n')
    }

    private fun loginUser() {
        viewModelScope.launch {
            try {
                val loginPayload = LoginPayload(
                    "tact1m4n3", "badpassword",
                )

                val result = ProHikingApi.retrofitService.loginUser(loginPayload)
                println(result)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: HttpException) {
                e.printStackTrace()
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

private val allTracks= listOf(
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