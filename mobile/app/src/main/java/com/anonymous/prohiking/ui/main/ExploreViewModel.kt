package com.anonymous.prohiking.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.TrailRepository
import com.anonymous.prohiking.data.model.Trail
import com.anonymous.prohiking.data.utils.ResultWrapper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ExploreViewModel(private val trailRepository: TrailRepository): ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _trails = MutableStateFlow(listOf<Trail>())
    @OptIn(FlowPreview::class)
    val trails = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_trails) { text, trails ->
            if (text.isBlank()) {
                trails
            } else {
                fetchTrails()
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _trails.value
        )

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    private suspend fun fetchTrails(): List<Trail> {
        val searchText = searchText.value
        return when (val result = trailRepository.searchTrails(
            5,
            0,
            searchText,
            0.0, 300.0,
            43.688444729, 20.2201924985, 48.2208812526, 29.62654341
        )) {
            is ResultWrapper.Success -> result.data
            is ResultWrapper.Error -> {
                println(result.error)
                listOf()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trailRepository = ProHikingApplication.instance.trailRepository
                ExploreViewModel(trailRepository = trailRepository)
            }
        }
    }
}
