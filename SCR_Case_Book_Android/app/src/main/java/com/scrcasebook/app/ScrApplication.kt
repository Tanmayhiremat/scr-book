package com.scrcasebook.app

import android.app.Application
import com.scrcasebook.app.data.db.ScrDatabase

class ScrApplication : Application() {
    val database: ScrDatabase by lazy {
        ScrDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ScrApplication
            private set
    }
}
