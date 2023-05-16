package com.anonymous.prohiking.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.TrailRepository
import com.anonymous.prohiking.data.model.Point
import com.anonymous.prohiking.data.utils.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NavigateViewModel(
    private val trailRepository: TrailRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val currentTrailId = preferencesRepository.trailId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), -1)

    val currentTrail = currentTrailId
        .map { id ->
            if (id == -1) {
                null
            } else {
                when (val result = trailRepository.getTrailById(id)) {
                    is Result.Success -> result.data
                    else -> null
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val currentTrailPath = currentTrailId
        .map { id ->
            if (id == -1) {
                null
            } else {
                when (val result = trailRepository.getTrailPath(id)) {
                    is Result.Success -> result.data
                    else -> null
                }
            }
        }
        .onEach { trailPath ->
            trailPath?.let { path ->
                focusOnTrailPath(path)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val cameraPositionState = CameraPositionState()

    fun focusOnCurrentTrail() {
        viewModelScope.launch {
            currentTrailPath.value?.let {trailPath ->
                focusOnTrailPath(trailPath)
            }
        }
    }

    private suspend fun focusOnTrailPath(trailPath: List<Point>) {
        val bounds = LatLngBounds.builder().also { builder ->
            for (point in trailPath) {
                builder.include(point.let { LatLng(it.lat, it.lon) })
            }
        }.build()

        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                100
            ))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trailRepository = ProHikingApplication.instance.trailRepository
                val preferencesRepository = ProHikingApplication.instance.preferencesRepository
                NavigateViewModel(trailRepository = trailRepository, preferencesRepository = preferencesRepository)
            }
        }
    }
}
