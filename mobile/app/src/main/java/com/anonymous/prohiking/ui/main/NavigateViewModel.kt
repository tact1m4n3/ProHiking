package com.anonymous.prohiking.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.anonymous.prohiking.ProHikingApplication
import com.anonymous.prohiking.data.PreferencesRepository
import com.anonymous.prohiking.data.TrailRepository
import com.anonymous.prohiking.ui.auth.LoginViewModel

class NavigateViewModel(private val trailRepository: TrailRepository, private val preferencesRepository: PreferencesRepository) : ViewModel() {
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
