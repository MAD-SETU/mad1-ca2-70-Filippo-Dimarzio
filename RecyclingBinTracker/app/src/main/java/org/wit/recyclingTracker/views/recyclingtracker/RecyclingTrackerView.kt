package org.wit.recyclingTracker.views.recyclingtracker

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerBinding
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import timber.log.Timber

class RecyclingTrackerView : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclingTrackerBinding
    private lateinit var presenter: RecyclingTrackerPresenter
    var recyclingTracker = RecyclingTrackerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRecyclingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = RecyclingTrackerPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheRecyclingTracker(binding.recyclingTrackerTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.recyclingTrackerLocation.setOnClickListener {
            presenter.cacheRecyclingTracker(binding.recyclingTrackerTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

        binding.btnAdd.setOnClickListener {
            if (binding.recyclingTrackerTitle.text.toString().isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_recyclingTracker_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(binding.recyclingTrackerTitle.text.toString(), binding.description.text.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_recycling_tracker, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showRecyclingTracker(recyclingTracker: RecyclingTrackerModel) {
        binding.recyclingTrackerTitle.setText(recyclingTracker.title)
        binding.description.setText(recyclingTracker.description)
        binding.btnAdd.setText(R.string.save_recyclingTracker)
        Picasso.get()
            .load(recyclingTracker.image)
            .into(binding.recyclingTrackerImage)
        if (recyclingTracker.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_recyclingTracker_image)
        }

    }

    fun updateImage(image: Uri){
        Timber.i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.recyclingTrackerImage)
        binding.chooseImage.setText(R.string.change_recyclingTracker_image)
    }

}