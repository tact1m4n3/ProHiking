package com.anonymous.prohiking.data

import com.anonymous.prohiking.data.network.newProHikingApiService

interface AppContainer {
    val userRepository: UserRepository
}

class AppContainerImpl: AppContainer {
    private val proHikingApiService = newProHikingApiService()

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(proHikingApiService)
    }
}