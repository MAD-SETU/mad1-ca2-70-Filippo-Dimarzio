package org.wit.recyclingTracker.main

import android.app.Application
import org.wit.recyclingTracker.models.RecyclingTrackerFileStore
import org.wit.recyclingTracker.models.RecyclingTrackerStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application(){

    lateinit var recyclingTrackers: RecyclingTrackerStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        recyclingTrackers = RecyclingTrackerFileStore(applicationContext)
        //recyclingTrackers = RecyclingTrackerMemStore()
        i("Recycling Tracker Application Started")
    }
}
