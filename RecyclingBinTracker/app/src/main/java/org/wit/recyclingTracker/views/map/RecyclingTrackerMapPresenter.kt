package org.wit.recyclingTracker.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.recyclingTracker.main.MainApp

class RecyclingTrackerMapPresenter(val view: RecyclingTrackerMapView) {

    var app: MainApp = view.application as MainApp

    fun doPopulateMap(map: GoogleMap) {
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(view)
        val list = app.recyclingTrackers.findAll()
        list.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options)?.tag = it.id
        }
        if (list.isNotEmpty()) {
            val last = list.last()
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(last.lat, last.lng), last.zoom))
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(52.245696, -7.139102), 15f))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as? Long
        if (tag != null) {
            val recyclingTracker = app.recyclingTrackers.findById(tag)
            if (recyclingTracker != null) {
                view.showRecyclingTracker(recyclingTracker)
            }
        }
    }

    fun doShowList() {
        view.finish()
    }
}
