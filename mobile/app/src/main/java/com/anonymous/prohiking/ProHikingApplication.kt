package com.anonymous.prohiking

import android.app.Application
import com.anonymous.prohiking.data.AppContainer
import com.anonymous.prohiking.data.AppContainerImpl

class ProHikingApplication: Application() {
    companion object {
        lateinit var instance: ProHikingApplication
            private set
    }

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        instance = this
        container = AppContainerImpl()
    }
}