package com.example.musicfreshener2

import android.app.Application
import android.content.Context
import com.example.musicfreshener2.data.AppContainer
import com.example.musicfreshener2.data.AppDataContainer

class MusicApplication : Application() {

    lateinit var container: AppContainer

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        container = AppDataContainer(this)
    }
}