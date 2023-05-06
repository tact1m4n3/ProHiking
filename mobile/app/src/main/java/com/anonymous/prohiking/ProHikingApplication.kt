package com.anonymous.prohiking

import android.app.Application

class ProHikingApplication: Application() {
    companion object {
        lateinit var instance: ProHikingApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}