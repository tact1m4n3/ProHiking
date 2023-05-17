package com.anonymous.prohiking.ui.main

import android.os.Handler
import android.os.Looper
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
import com.anonymous.prohiking.data.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NavigateViewModel(
    private val trailRepository: TrailRepository,
    private val preferencesRepository: PreferencesRepository,
    private val locationClient: LocationClient
) : ViewModel() {
    private val currentTrailId = preferencesRepository.trailId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), -1)

    val currentTrailTime = preferencesRepository.trailTime
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
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    init {
//        val mainHandler = Handler(Looper.getMainLooper())
//        mainHandler.post(object : Runnable {
//            override fun run() {
//                println(currentTrailTime.value)
//                if (currentTrailTime.value != -1L && currentTrailTime.value % 5000 == 0L) {
//                    viewModelScope.launch {
//                        preferencesRepository.updateTrailTime(currentTrailTime.value + 1000)
//                    }
//                    mainHandler.postDelayed(this, 1000)
//                }
//            }
//        })
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trailRepository = ProHikingApplication.instance.trailRepository
                val preferencesRepository = ProHikingApplication.instance.preferencesRepository
                val locationClient = ProHikingApplication.instance.locationClient
                NavigateViewModel(
                    trailRepository = trailRepository,
                    preferencesRepository = preferencesRepository,
                    locationClient = locationClient
                )
            }
        }
    }
}
