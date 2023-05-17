package com.anonymous.prohiking.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.LocationClient
import com.anonymous.prohiking.data.LocationDetails
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.TrailRepository
import com.anonymous.prohiking.data.model.Point
import com.anonymous.prohiking.data.model.Trail
import com.anonymous.prohiking.data.utils.Result
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val trailRepository: TrailRepository,
    private val preferencesRepository: PreferencesRepository,
    private val locationClient: LocationClient
): ViewModel() {
    private val location = locationClient
        .getLocationUpdates(10000L)
        .catch { e -> e.printStackTrace() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocationDetails())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val recommendedTrails = location
        .dropWhile { location -> location.latitude == 0.0 && location.longitude == 0.0 }
        .map { location -> loadRecommendedTrails(location) }
        .onEach { _isLoading.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchedTrails = MutableStateFlow(listOf<Trail>())
    @OptIn(FlowPreview::class)
    val searchedTrails = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_searchedTrails) { text, trails ->
            if (text.isBlank()) {
                trails
            } else {
                searchTrails(text)
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _searchedTrails.value
        )

    private val _selectedTrail = MutableStateFlow<Trail?>(null)
    val selectedTrail = _selectedTrail.asStateFlow()

    private val _selectedTrailPath = MutableStateFlow<List<Point>?>(null)
    val selectedTrailPath = _selectedTrailPath.asStateFlow()

    private suspend fun loadRecommendedTrails(location: LocationDetails): List<Trail> {
        return when (val result = trailRepository.searchTrails(
            5,
            0,
            "",
            0.0, 300.0,
            location.latitude, location.longitude,
            1.0,
        )) {
            is Result.Success -> result.data
            is Result.Error -> {
                println(result.error)
                listOf()
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun onTrailSelect(trail: Trail) {
        _selectedTrail.value = trail
        viewModelScope.launch {
            _selectedTrailPath.value = when (val result = trailRepository.getTrailPath(trail.id)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
        }
    }

    private suspend fun searchTrails(name: String): List<Trail> {
        return when (val result = trailRepository.searchTrails(
            5,
            0,
            name,
            0.0, 300.0,
            location.value.latitude, location.value.longitude,
            200.0
        )) {
            is Result.Success -> result.data
            is Result.Error -> {
                println(result.error)
                listOf()
            }
        }
    }

    fun onStartTrailButtonPressed(trail: Trail) {
        viewModelScope.launch {
            if (preferencesRepository.trailId.first() != trail.id) {
                preferencesRepository.updateTrailId(trail.id)
                preferencesRepository.updateTrailTime(0L)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trailRepository = ProHikingApplication.instance.trailRepository
                val preferencesRepository = ProHikingApplication.instance.preferencesRepository
                val locationClient = ProHikingApplication.instance.locationClient
                ExploreViewModel(
                    trailRepository = trailRepository,
                    preferencesRepository = preferencesRepository,
                    locationClient = locationClient
                )
            }
        }
    }
}
