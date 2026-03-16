package org.wit.recyclingTracker.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.*

class RecyclingTrackerFileStore(val context: Context) : RecyclingTrackerStore {

    private var recyclingTrackers = mutableListOf<RecyclingTrackerModel>()
    private val fileName = "recyclingTrackers.json"
    private val gson = Gson()

    init {
        load()
    }

    override fun findAll(): List<RecyclingTrackerModel> {
        return recyclingTrackers
    }

    override fun create(recyclingTracker: RecyclingTrackerModel) {
        recyclingTracker.id = Random().nextLong()
        recyclingTrackers.add(recyclingTracker)
        serialize()
    }

    override fun update(recyclingTracker: RecyclingTrackerModel) {
        val found = recyclingTrackers.find { it.id == recyclingTracker.id }
        if (found != null) {
            found.title = recyclingTracker.title
            found.description = recyclingTracker.description
            found.date = recyclingTracker.date
            found.image = recyclingTracker.image
            serialize()
        }
    }

    override fun delete(recyclingTracker: RecyclingTrackerModel) {
        val found = recyclingTrackers.find { it.id == recyclingTracker.id }
        if (found != null) {
            recyclingTrackers.remove(found)
            serialize()
        }
    }

    private fun serialize() {
        val jsonString = gson.toJson(recyclingTrackers)
        File(context.filesDir, fileName).writeText(jsonString)
    }

    private fun load() {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            val jsonString = file.readText()
            val type = object : TypeToken<MutableList<RecyclingTrackerModel>>() {}.type
            recyclingTrackers = gson.fromJson(jsonString, type)
        }
    }
}
