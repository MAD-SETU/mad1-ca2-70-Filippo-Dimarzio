package org.wit.recyclingTracker.views.recyclingtracker

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.recyclingTracker.R
import org.wit.recyclingTracker.databinding.ActivityRecyclingTrackerBinding
import org.wit.recyclingTracker.models.RecyclingTrackerModel
import org.wit.recyclingTracker.views.BaseView

class RecyclingTrackerView : BaseView() {

    private lateinit var binding: ActivityRecyclingTrackerBinding
    private lateinit var presenter: RecyclingTrackerPresenter
    var recyclingTracker = RecyclingTrackerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclingTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = RecyclingTrackerPresenter(this)
        basePresenter = presenter

        if (presenter.edit) {
            binding.toolbarAdd.title = getString(R.string.save_recyclingTracker)
        } else {
            binding.toolbarAdd.title = getString(R.string.button_addRecyclingTracker)
        }
        initToolbar(binding.toolbarAdd, true)

        if (!presenter.edit) {
            Picasso.get().load(R.drawable.recycling_logo).into(binding.recyclingTrackerImage)
        }

        binding.chooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        binding.recyclingTrackerLocation.setOnClickListener {
            presenter.doSetLocation()
        }

        binding.btnAdd.setOnClickListener {
            val title = binding.recyclingTrackerTitle.text.toString()
            val description = binding.description.text.toString()
            val company = binding.company.text.toString()
            val date = binding.dateAdded.text.toString()

            if (title.isEmpty()) {
                Snackbar.make(binding.root, R.string.enter_recyclingTracker_title, Snackbar.LENGTH_LONG).show()
            } else {
                presenter.doAddOrSave(title, description, company, date)
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
            R.id.item_delete -> presenter.doDelete()
            R.id.item_cancel -> presenter.doCancel()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showRecyclingTracker(recyclingTracker: RecyclingTrackerModel) {
        binding.recyclingTrackerTitle.setText(recyclingTracker.title)
        binding.description.setText(recyclingTracker.description)
        binding.company.setText(recyclingTracker.company)
        binding.dateAdded.setText(recyclingTracker.date)

        if (recyclingTracker.image != Uri.EMPTY) {
            Picasso.get().load(recyclingTracker.image).into(binding.recyclingTrackerImage)
        } else {
            Picasso.get().load(R.drawable.recycling_logo).into(binding.recyclingTrackerImage)
        }
        binding.btnAdd.setText(R.string.save_recyclingTracker)
    }

    override fun updateImage(image: Uri) {
        Picasso.get().load(image).into(binding.recyclingTrackerImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }
}
