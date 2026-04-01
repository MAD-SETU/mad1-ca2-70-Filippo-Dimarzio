package org.wit.recyclingTracker.models

interface RecyclingTrackerStore {
    fun findAll(): List<RecyclingTrackerModel>
    fun create(recyclingTracker: RecyclingTrackerModel)
    fun update(recyclingTracker: RecyclingTrackerModel)
    fun delete(recyclingTracker: RecyclingTrackerModel)
    fun findById(id: Long): RecyclingTrackerModel?
}
