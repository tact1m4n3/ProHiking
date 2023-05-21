package com.anonymous.prohiking.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.AltitudeRepository
import com.anonymous.prohiking.data.LocationClient
import com.anonymous.prohiking.data.LocationDetails
import com.anonymous.prohiking.data.OfflineTrailRepository
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.TrailRepository
import com.anonymous.prohiking.data.WeatherRepository
import com.anonymous.prohiking.data.network.AltitudeApiModel
import com.anonymous.prohiking.data.network.PointApiModel
import com.anonymous.prohiking.data.network.TrailApiModel
import com.anonymous.prohiking.data.network.WeatherDataApiModel
import com.anonymous.prohiking.data.network.utils.ApiResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
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
import kotlin.math.abs

class ExploreViewModel(
    private val trailRepository: TrailRepository,
    private val offlineTrailRepository: OfflineTrailRepository,
    private val weatherRepository: WeatherRepository,
    private val altitudeRepository: AltitudeRepository,
    private val preferencesRepository: PreferencesRepository,
    private val locationClient: LocationClient
): ViewModel() {
    private val location = locationClient
        .getLocationUpdates(30000)
        .catch { e -> e.printStackTrace() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocationDetails())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val recommendedTrails = location
        .dropWhile { location -> location.latitude == 0.0 && location.longitude == 0.0 }
        .onEach {
            _isLoading.update { true }
            println(location)
        }
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

    private val _searchedTrails = MutableStateFlow(listOf<TrailApiModel>())
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

    val allTrails = mutableStateListOf<TrailWrapper>()

    private val _selectedTrail = MutableStateFlow<TrailApiModel?>(null)
    val selectedTrail = _selectedTrail.asStateFlow()

    private val _selectedTrailPath = MutableStateFlow<List<PointApiModel>?>(null)
    val selectedTrailPath = _selectedTrailPath.asStateFlow()

    private val _selectedTrailWeather = MutableStateFlow<WeatherDataApiModel?>(null)
    val selectedTrailWeather = _selectedTrailWeather.asStateFlow()

    private val _selectedTrailAltitudeDifference = MutableStateFlow<Int?>(null)
    val selectedTrailAltitudeDifference = _selectedTrailAltitudeDifference.asStateFlow()

    private suspend fun loadRecommendedTrails(location: LocationDetails): List<TrailApiModel> {
        return when (val result = trailRepository.searchTrails(
            5,
            0,
            "",
            0.0, 300.0,
            location.latitude, location.longitude,
            4.0,
        )) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> {
                println(result.error)
                listOf()
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun onTrailSelect(trail: TrailApiModel) {
        _selectedTrail.value = trail
        viewModelScope.launch {
            _selectedTrailPath.value = when (val result = trailRepository.getTrailPath(trail.id)) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> null
            }

            _selectedTrailPath.value?.let { trailPath ->
                val startPoint = trailPath[0]
                val endPoint = trailPath[trailPath.size - 1]

                _selectedTrailWeather.value = when (val result = weatherRepository.getWeatherData(
                    endPoint.lat,
                    endPoint.lon
                )) {
                    is ApiResult.Success -> result.data
                    is ApiResult.Error -> null
                }

                _selectedTrailAltitudeDifference.value = when (val result = altitudeRepository.getAltitude(
                    listOf(startPoint.lat, endPoint.lat),
                    listOf(startPoint.lon, endPoint.lon)
                )) {
                    is ApiResult.Success -> abs(result.data.elevation[1] - result.data.elevation[0]).toInt()
                    is ApiResult.Error -> null
                }
            }
        }
    }

    private suspend fun searchTrails(name: String): List<TrailApiModel> {
        return when (val result = trailRepository.searchTrails(
            5,
            0,
            name,
            0.0, 300.0,
            location.value.latitude, location.value.longitude,
            1000.0
        )) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> {
                println(result.error)
                listOf()
            }
        }
    }

    fun onStartTrailButtonPressed() {
        viewModelScope.launch {
            selectedTrail.value?.let { trail ->
                selectedTrailPath.value?.let { trailPath ->
                    val oldTrailId = preferencesRepository.trailId.first()

                    if (oldTrailId != trail.id) {
                        saveCurrentTrailLocally(trail, trailPath)
                        preferencesRepository.updateTrailId(trail.id)
                    }

                    if (oldTrailId != -1) {
                        deleteOldTrailLocally(oldTrailId)
                    }
                }
            }
        }
    }

    private suspend fun saveCurrentTrailLocally(trail: TrailApiModel, trailPath: List<PointApiModel>) {
        val trailEntity = trail.toEntity()
        val trailPathEntity = trailPath.map { it.toEntity(trail.id) }
        offlineTrailRepository.insertTrail(trailEntity)
        offlineTrailRepository.insertTrailPath(trailPathEntity)
    }

    private suspend fun deleteOldTrailLocally(id: Int) {
        offlineTrailRepository.deleteTrail(id)
        offlineTrailRepository.deleteTrailPath(id)
    }

    suspend fun fetchNextTrails(limit: Int, center: LatLng, radius: Double): Boolean {
        return when (val result = trailRepository.searchTrails(
            limit,
            allTrails.size,
            "",
            0.0,
            20.0,
            center.latitude,
            center.longitude,
            radius,
        )) {
            is ApiResult.Success -> {
                val newTrails = result.data
                allTrails.addAll(newTrails.map { TrailWrapper(it) })
                newTrails.isNotEmpty()
            }
            is ApiResult.Error -> {
                println(result.error)
                true
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trailRepository = ProHikingApplication.instance.trailRepository
                val offlineTrailRepository = ProHikingApplication.instance.offlineTrailRepository
                val weatherRepository = ProHikingApplication.instance.weatherRepository
                val altitudeRepository = ProHikingApplication.instance.altitudeRepository
                val preferencesRepository = ProHikingApplication.instance.preferencesRepository
                val locationClient = ProHikingApplication.instance.locationClient
                ExploreViewModel(
                    trailRepository = trailRepository,
                    offlineTrailRepository = offlineTrailRepository,
                    weatherRepository = weatherRepository,
                    altitudeRepository = altitudeRepository,
                    preferencesRepository = preferencesRepository,
                    locationClient = locationClient
                )
            }
        }
    }
}

class TrailWrapper(
    val inner: TrailApiModel,
): ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(inner.point.lat, inner.point.lon)
    }

    override fun getTitle(): String {
        return inner.name
    }

    override fun getSnippet(): String? {
        return null
    }
}
