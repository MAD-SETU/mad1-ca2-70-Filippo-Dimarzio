package org.wit.recyclingTracker.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.adapters.RecyclingTrackerAdapter
import org.wit.recyclingTracker.adapters.RecyclingTrackerListener
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerListBinding
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.RecyclingTrackerModel

class RecyclingTrackersListActivity : AppCompatActivity(), RecyclingTrackerListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityRecyclingTrackerListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclingTrackerListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadTrackers()
        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, RecyclingTrackerActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, RecyclingTrackerMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }



    override fun onRecyclingTrackerClick(recycling: RecyclingTrackerModel, position: Int) {
        this.position = position
        val launcherIntent = Intent(this, RecyclingTrackerActivity::class.java)
        launcherIntent.putExtra("recycling_tracker_edit", recycling)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == RESULT_OK) {
                    loadTrackers()
                } else if (result.resultCode == 99) {
                    (binding.recyclerView.adapter)?.notifyItemRemoved(position)
                }
            }
    }

    private fun loadTrackers() {
        showTrackers(app.recyclingTrackers.findAll())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showTrackers(trackers: List<RecyclingTrackerModel>) {
        binding.recyclerView.adapter = RecyclingTrackerAdapter(trackers, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}
