package org.wit.recyclingTracker.views.map

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerMapsBinding
import org.wit.recyclingTracker.databinding.ContentRecyclingTrackerMapsBinding
import org.wit.recyclingTracker.main.MainApp
import org.wit.recyclingTracker.models.RecyclingTrackerModel

class RecyclingTrackerMapView : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

    private lateinit var binding: ActivityRecyclingTrackerMapsBinding
    private lateinit var contentBinding: ContentRecyclingTrackerMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: RecyclingTrackerMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityRecyclingTrackerMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter = RecyclingTrackerMapPresenter(this)

        contentBinding = binding.contentMaps

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            presenter.doPopulateMap(it)
        }
    }
    fun showRecyclingTracker(recyclingTracker: RecyclingTrackerModel) {
        contentBinding.currentTitle.text = recyclingTracker.title
        contentBinding.currentDescription.text = recyclingTracker.description
        contentBinding.currentCompany.text = recyclingTracker.company
        if (recyclingTracker.image.toString().isNotEmpty()) {
            Picasso.get()
                .load(recyclingTracker.image)
                .into(contentBinding.currentImage)
        } else {
            contentBinding.currentImage.setImageResource(R.drawable.recycling_map_logo)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_list -> {
                presenter.doShowList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}
