package org.wit.recyclingTracker.views.recylingtrackerlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerListBinding
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import org.wit.recyclingTracker.views.login.LoginView

import org.wit.recyclingTracker.views.BaseView
import org.wit.recyclingTracker.views.VIEW

class RecyclingTrackerListView : BaseView(), RecyclingTrackerListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityRecyclingTrackerListBinding
    lateinit var presenter: RecyclingTrackerListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclingTrackerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar, false)

        presenter = RecyclingTrackerListPresenter(this)
        basePresenter = presenter

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadRecyclingTrackers()

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add -> { presenter.doAddRecyclingTracker() }
            R.id.nav_map -> { presenter.doShowRecyclingTrackersMap() }
            R.id.nav_logout -> {
                navigateWithView(VIEW.LOGIN, 0)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
        presenter.doEditRecyclingTracker(recycling, position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) onRefresh()
        else if (resultCode == 99) {
            // position is handled in the presenter in the lab, but here we just refresh
            onRefresh()
        }
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
