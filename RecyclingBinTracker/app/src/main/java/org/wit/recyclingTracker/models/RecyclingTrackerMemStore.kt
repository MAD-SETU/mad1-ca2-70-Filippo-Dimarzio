package org.wit.recyclingTracker.models

import timber.log.Timber.Forest.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class RecyclingTrackerMemStore : RecyclingTrackerStore {

    val recyclingTrackers = ArrayList<RecyclingTrackerModel>()

    override fun findAll(): List<RecyclingTrackerModel> {
        return recyclingTrackers
    }

    override fun delete(recyclingTracker: RecyclingTrackerModel) {
        recyclingTrackers.remove(recyclingTracker)
        serialize()
    }

    override fun create(recyclingTracker: RecyclingTrackerModel) {
        recyclingTracker.id = getId()
        recyclingTrackers.add(recyclingTracker)
        serialize()
    }

    override fun update(recyclingTracker: RecyclingTrackerModel) {
        val foundRecyclingTracker: RecyclingTrackerModel? =
            recyclingTrackers.find { p -> p.id == recyclingTracker.id }
        if (foundRecyclingTracker != null) {
            foundRecyclingTracker.title = recyclingTracker.title
            foundRecyclingTracker.description = recyclingTracker.description
            foundRecyclingTracker.company = recyclingTracker.company
            foundRecyclingTracker.date = recyclingTracker.date
            foundRecyclingTracker.image = recyclingTracker.image
            foundRecyclingTracker.lat = recyclingTracker.lat
            foundRecyclingTracker.lng = recyclingTracker.lng
            foundRecyclingTracker.zoom = recyclingTracker.zoom
            serialize()
        }
    }

    private fun serialize() {
        logAll()
    }

    private fun logAll() {
        recyclingTrackers.forEach { i("$it") }
    }

    override fun findById(id: Long): RecyclingTrackerModel? {
        return recyclingTrackers.find { it.id == id }
    }
}
