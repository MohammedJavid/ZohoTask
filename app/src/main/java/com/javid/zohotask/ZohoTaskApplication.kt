package com.javid.zohotask

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZohoTaskApplication: Application() {

    companion object {
        var instance: ZohoTaskApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}