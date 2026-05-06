package org.wit.recyclingTracker.views

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.wit.recyclingTracker.models.Location
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import org.wit.recyclingTracker.views.editLocation.EditLocationView
import org.wit.recyclingTracker.views.login.LoginView
import org.wit.recyclingTracker.views.map.RecyclingTrackerMapView
import org.wit.recyclingTracker.views.recyclingtracker.RecyclingTrackerView
import org.wit.recyclingTracker.views.recylingtrackerlist.RecyclingTrackerListView

enum class VIEW {
    LOCATION, RECYCLINGTRACKER, MAP, LIST, LOGIN
}

open abstract class BaseView : AppCompatActivity() {

    var basePresenter: BasePresenter? = null

    fun navigateWithView(view: VIEW, code: Int, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, RecyclingTrackerListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.RECYCLINGTRACKER -> intent = Intent(this, RecyclingTrackerView::class.java)
            VIEW.MAP -> intent = Intent(this, RecyclingTrackerMapView::class.java)
            VIEW.LIST -> intent = Intent(this, RecyclingTrackerListView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
        }
        if (key != "") intent.putExtra(key, value)
        startActivityForResult(intent, code)
    }

    fun initToolbar(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    }

    open fun showRecyclingTracker(recyclingTracker: RecyclingTrackerModel) {}
    open fun showRecyclingTrackers(recyclingTrackers: List<RecyclingTrackerModel>) {}
    open fun updateImage(uri: android.net.Uri) {}
    open fun updateLocation(location: Location) {}
}
