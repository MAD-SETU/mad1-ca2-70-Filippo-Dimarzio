package org.wit.recyclingTracker.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerBinding
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.Location
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import timber.log.Timber.Forest.i

class RecyclingTrackerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclingTrackerBinding
    var recyclingTracker = RecyclingTrackerModel()
    lateinit var app: MainApp

    private lateinit var imageIntentLauncher : ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityRecyclingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Recycling Tracker Activity started...")

        if (intent.hasExtra("recycling_tracker_edit")) {
            edit = true
            recyclingTracker =
                intent.getParcelableExtra<RecyclingTrackerModel>("recycling_tracker_edit")!!
            binding.recyclingTrackerTitle.setText(recyclingTracker.title)
            binding.description.setText(recyclingTracker.description)
            binding.dateAdded.setText(recyclingTracker.date)
            binding.btnAdd.setText(R.string.save_recyclingTracker)
            binding.btnDelete.visibility = View.VISIBLE
            
            Picasso.get()
                .load(recyclingTracker.image)
                .into(binding.recyclingTrackerImage)
            if (recyclingTracker.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_recyclingTracker_image)
            }
        }

        binding.btnAdd.setOnClickListener {
            recyclingTracker.title = binding.recyclingTrackerTitle.text.toString()
            recyclingTracker.description = binding.description.text.toString()
            recyclingTracker.date = binding.dateAdded.text.toString()

            if (recyclingTracker.title.isNotEmpty()) {
                if (edit) {
                    app.recyclingTrackers.update(recyclingTracker.copy())
                } else {
                    app.recyclingTrackers.create(recyclingTracker.copy())
                }

                i("add Button Pressed: $recyclingTracker")
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar
                    .make(it, R.string.enter_recyclingTracker_title, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnDelete.setOnClickListener {
            app.recyclingTrackers.delete(recyclingTracker)
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            val request = PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                .build()
            imageIntentLauncher.launch(request)
        }

        binding.recyclingTrackerLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if(recyclingTracker.zoom != 0f){
                location.lat = recyclingTracker.lat
                location.lng = recyclingTracker.lng
                location.zoom = recyclingTracker.zoom
            }
            val launcherIntent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_recycling_tracker, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    recyclingTracker.image = uri
                    i("IMG :: ${recyclingTracker.image}")
                    Picasso.get()
                        .load(recyclingTracker.image)
                        .into(binding.recyclingTrackerImage)
                    binding.chooseImage.setText(R.string.change_recyclingTracker_image)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            recyclingTracker.lat = location.lat
                            recyclingTracker.lng = location.lng
                            recyclingTracker.zoom = location.zoom
                        }
                    }
                    RESULT_CANCELED -> { }
                    else -> { }
                }
            }
    }
}
