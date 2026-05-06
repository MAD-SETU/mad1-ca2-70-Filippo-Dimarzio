package org.wit.recyclingTracker.views.recyclingtracker

import android.app.Activity
import android.content.Intent
import org.wit.recyclingTracker.models.Location
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import org.wit.recyclingTracker.views.BasePresenter
import org.wit.recyclingTracker.views.BaseView
import org.wit.recyclingTracker.views.VIEW

class RecyclingTrackerPresenter(view: BaseView) : BasePresenter(view) {

    var recyclingTracker = RecyclingTrackerModel()
    var edit = false

    init {
        if (view.intent.hasExtra("recycling_tracker_edit")) {
            edit = true
            recyclingTracker = view.intent.extras?.getParcelable("recycling_tracker_edit")!!
            view.showRecyclingTracker(recyclingTracker)
        }
    }

    fun doAddOrSave(title: String, description: String, company: String, date: String) {
        recyclingTracker.title = title
        recyclingTracker.description = description
        recyclingTracker.company = company
        recyclingTracker.date = date
        if (edit) {
            app.recyclingTrackers.update(recyclingTracker.copy())
        } else {
            app.recyclingTrackers.create(recyclingTracker.copy())
        }
        view?.setResult(Activity.RESULT_OK)
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        app.recyclingTrackers.delete(recyclingTracker)
        view?.setResult(Activity.RESULT_OK)
        view?.finish()
    }

    fun doSelectImage() {
        // Lab style usually uses a simpler intent, but keeping your logic within the MVP structure
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        view?.startActivityForResult(intent, 1)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (recyclingTracker.zoom != 0f) {
            location.lat = recyclingTracker.lat
            location.lng = recyclingTracker.lng
            location.zoom = recyclingTracker.zoom
        }
        view?.navigateWithView(VIEW.LOCATION, 2, "location", location)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> {
                if (data != null) {
                    val uri = data.data!!
                    view?.contentResolver?.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    recyclingTracker.image = uri
                    view?.updateImage(recyclingTracker.image)
                }
            }
            2 -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    recyclingTracker.lat = location.lat
                    recyclingTracker.lng = location.lng
                    recyclingTracker.zoom = location.zoom
                    android.widget.Toast.makeText(view, "Location Updated!", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
