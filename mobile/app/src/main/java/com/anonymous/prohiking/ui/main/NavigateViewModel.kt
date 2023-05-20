package com.anonymous.prohiking.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.LocationClient
import com.anonymous.prohiking.data.LocationDetails
import com.anonymous.prohiking.data.OfflineTrailRepository
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.local.PointEntity
import com.anonymous.prohiking.data.local.TrailEntity
import com.anonymous.prohiking.data.network.PointApiModel
import com.anonymous.prohiking.data.network.TrailApiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NavigateViewModel(
    private val offlineTrailRepository: OfflineTrailRepository,
    private val preferencesRepository: PreferencesRepository,
    private val locationClient: LocationClient
) : ViewModel() {
    private val currentTrailId = preferencesRepository.trailId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), -1)

    val location = locationClient
        .getLocationUpdates(1000)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            LocationDetails()
        )

    val currentTrail = currentTrailId
        .map { id ->
            if (id == -1) {
                null
            } else {
                offlineTrailRepository.getTrailById(id).first()
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
                offlineTrailRepository.getTrailPath(id).first()?.let { trailPath ->
                    trailPath.ifEmpty {
                        null
                    }
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    fun onStopTrailButtonClick() {
        viewModelScope.launch {
            currentTrail.first()?.let { trail ->
                currentTrailPath.first()?.let { trailPath ->
                    deleteCurrentTrailLocally(trail, trailPath)
                }
            }
            preferencesRepository.updateTrailId(-1)
        }
    }

    private suspend fun deleteCurrentTrailLocally(trail: TrailEntity, trailPath: List<PointEntity>) {
        offlineTrailRepository.deleteTrail(trail)
        offlineTrailRepository.deleteTrailPath(trailPath)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val offlineTrailRepository = ProHikingApplication.instance.offlineTrailRepository
                val preferencesRepository = ProHikingApplication.instance.preferencesRepository
                val locationClient = ProHikingApplication.instance.locationClient
                NavigateViewModel(
                    offlineTrailRepository = offlineTrailRepository,
                    preferencesRepository = preferencesRepository,
                    locationClient = locationClient
                )
            }
        }
    }
}
