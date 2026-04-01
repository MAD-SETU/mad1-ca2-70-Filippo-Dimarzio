package org.wit.recyclingTracker.views.recylingtrackerlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerListBinding
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.RecyclingTrackerModel

class RecyclingTrackerListView : AppCompatActivity(), RecyclingTrackerListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityRecyclingTrackerListBinding
    lateinit var presenter: RecyclingTrackerListPresenter
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclingTrackerListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = RecyclingTrackerListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadRecyclingTrackers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddRecyclingTracker() }
            R.id.item_map -> { presenter.doShowRecyclingTrackersMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecyclingTrackerClick(recycling: RecyclingTrackerModel, position: Int) {
        this.position = position
        presenter.doEditRecyclingTracker(recycling, this.position)
    }

    private fun loadRecyclingTrackers() {
        binding.recyclerView.adapter = RecyclingTrackerAdapter(presenter.getRecyclingTrackers(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,presenter.getRecyclingTrackers().size)
    }

    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }
}
