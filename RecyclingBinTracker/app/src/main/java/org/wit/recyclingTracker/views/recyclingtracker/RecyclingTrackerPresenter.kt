package org.wit.recyclingTracker.views.recyclingtracker

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.Location
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import org.wit.recyclingTracker.views.editLocation.EditLocationView
import timber.log.Timber

class RecyclingTrackerPresenter(val view: RecyclingTrackerView) {

    var recyclingTracker = RecyclingTrackerModel()
    var app: MainApp = view.application as MainApp
    var edit = false
    private lateinit var imageIntentLauncher : ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        if (view.intent.hasExtra("recycling_tracker_edit")) {
            edit = true
            recyclingTracker = view.intent.extras?.getParcelable("recycling_tracker_edit")!!
            view.showRecyclingTracker(recyclingTracker)
        }
        registerImagePickerCallback()
        registerMapCallback()
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
        view.setResult(Activity.RESULT_OK)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doDelete() {
        app.recyclingTrackers.delete(recyclingTracker)
        view.setResult(99)
        view.finish()
    }

    fun doSelectImage() {
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
            .build()
        imageIntentLauncher.launch(request)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (recyclingTracker.zoom != 0f) {
            location.lat = recyclingTracker.lat
            location.lng = recyclingTracker.lng
            location.zoom = recyclingTracker.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheRecyclingTracker (title: String, description: String, company: String, date: String) {
        recyclingTracker.title = title
        recyclingTracker.description = description
        recyclingTracker.company = company
        recyclingTracker.date = date
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = view.registerForActivityResult(ActivityResultContracts.PickVisualMedia())
        { uri ->
            if (uri != null) {
                try{
                    view.contentResolver
                        .takePersistableUriPermission(uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION )
                    recyclingTracker.image = uri
                    Timber.i("IMG :: ${recyclingTracker.image}")
                    view.updateImage(recyclingTracker.image)
                }
                catch(e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun registerMapCallback() {
        mapIntentLauncher = view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    if (result.data != null) {
                        Timber.i("Got Location ${result.data.toString()}")
                        //val location = result.data!!.extras?.getParcelable("location",Location::class.java)!!
                        val location = result.data!!.extras?.getParcelable<Location>("location")!!
                        Timber.i("Location == $location")
                        recyclingTracker.lat = location.lat
                        recyclingTracker.lng = location.lng
                        recyclingTracker.zoom = location.zoom
                    }
                }
                AppCompatActivity.RESULT_CANCELED -> { } else -> { }
            }
        }
    }
}
