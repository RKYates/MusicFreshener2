package com.example.musicfreshener2.data

import android.content.Context

/**
 * App container for Dependency Injection.
 */
interface AppContainer {
    val musicRepository: MusicRepository
}

/**
 * [AppContainer] implementation to provide instance of [OfflineMusicRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val musicRepository: MusicRepository by lazy {
        OfflineMusicRepository(MusicDatabase.getDatabase(context).musicDao())
    }
}