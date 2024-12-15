package com.locado.app

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        Places.initializeWithNewPlacesApiEnabled(applicationContext,applicationContext.getString(R.string.places_api_key))
    }


}