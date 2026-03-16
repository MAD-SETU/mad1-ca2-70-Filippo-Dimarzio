package org.wit.recyclingTracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerBinding
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import timber.log.Timber

class RecyclingTrackerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclingTrackerBinding
    var recyclingTracker = RecyclingTrackerModel()
    lateinit var app: MainApp
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        Timber.i("Recycling Tracker Activity started...")

        if (intent.hasExtra("recycling_tracker_edit")) {
            edit = true
            recyclingTracker = intent.extras?.getParcelable("recycling_tracker_edit")!!
            binding.recyclingTrackerTitle.setText(recyclingTracker.title)
            binding.description.setText(recyclingTracker.description)
            binding.dateAdded.setText(recyclingTracker.date)
            binding.btnAdd.setText(R.string.save_recyclingTracker)
            binding.btnDelete.visibility = View.VISIBLE
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
                Timber.i("add Button Pressed: $recyclingTracker")
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,R.string.enter_recyclingTracker_title, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnDelete.setOnClickListener {
            app.recyclingTrackers.delete(recyclingTracker)
            setResult(RESULT_OK)
            finish()
        }
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
}
