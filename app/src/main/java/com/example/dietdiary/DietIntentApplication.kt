package com.example.dietdiary

import android.app.Application

class DietIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DietRepository.initialize(this)
    }
}