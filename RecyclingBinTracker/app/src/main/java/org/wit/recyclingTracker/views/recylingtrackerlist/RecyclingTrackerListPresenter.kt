package org.wit.recyclingTracker.views.recylingtrackerlist

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import org.wit.recyclingTracker.views.map.RecyclingTrackerMapView
import org.wit.recyclingTracker.views.recyclingtracker.RecyclingTrackerView

import org.wit.recyclingTracker.views.BasePresenter
import org.wit.recyclingTracker.views.BaseView
import org.wit.recyclingTracker.views.VIEW

class RecyclingTrackerListPresenter(view: BaseView) : BasePresenter(view) {

    fun getRecyclingTrackers() = app.recyclingTrackers.findAll()

    fun doAddRecyclingTracker() {
        view?.navigateWithView(VIEW.RECYCLINGTRACKER, 0)
    }

    fun doEditRecyclingTracker(recyclingTracker: RecyclingTrackerModel, pos: Int) {
        view?.navigateWithView(VIEW.RECYCLINGTRACKER, 0, "recycling_tracker_edit", recyclingTracker)
    }

    fun doShowRecyclingTrackersMap() {
        view?.navigateWithView(VIEW.MAP, 0)
    }
}
