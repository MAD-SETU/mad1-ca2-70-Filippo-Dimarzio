package org.wit.recyclingTracker.views.editLocation

import android.app.Activity
import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.recyclingTracker.models.Location
import org.wit.recyclingTracker.views.BasePresenter
import org.wit.recyclingTracker.views.BaseView

class EditLocationPresenter(view: BaseView) : BasePresenter(view) {

    var location = Location()

    init {
        location = view.intent.extras?.getParcelable("location")!!
    }

    fun doInitMap(map: GoogleMap) {
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Recycling Tracker")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    fun doUpdateLocation(lat: Double, lng: Double, zoom: Float) {
        location.lat = lat
        location.lng = lng
        location.zoom = zoom
    }

    fun doSave() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        view?.setResult(Activity.RESULT_OK, resultIntent)
        view?.finish()
    }

    fun doUpdateMarker(marker: Marker) {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
    }
}
