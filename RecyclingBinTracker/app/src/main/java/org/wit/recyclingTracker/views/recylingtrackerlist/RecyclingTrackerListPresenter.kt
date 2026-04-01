package org.wit.recyclingTracker.views.recylingtrackerlist

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import org.wit.recyclingTracker.views.map.RecyclingTrackerMapView
import org.wit.recyclingTracker.views.recyclingtracker.RecyclingTrackerView

class RecyclingTrackerListPresenter(val view: RecyclingTrackerListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getRecyclingTrackers() = app.recyclingTrackers.findAll()

    fun doAddRecyclingTracker() {
        val launcherIntent = Intent(view, RecyclingTrackerView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditRecyclingTracker(recyclingTracker: RecyclingTrackerModel, pos: Int) {
        val launcherIntent = Intent(view, RecyclingTrackerView::class.java)
        launcherIntent.putExtra("recycling_tracker_edit", recyclingTracker)
        position = pos
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doShowRecyclingTrackersMap() {
        val launcherIntent = Intent(view, RecyclingTrackerMapView::class.java)
        mapIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) view.onRefresh()
                else
                    if (it.resultCode == 99) view.onDelete(position)
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}
