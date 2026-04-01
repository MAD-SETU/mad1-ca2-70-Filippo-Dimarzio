package org.wit.recyclingTracker.models

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import org.wit.recyclingTracker.helpers.exists
import org.wit.recyclingTracker.helpers.read
import org.wit.recyclingTracker.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.math.log

const val JSON_FILE = "recyclingTrackers.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
                        .registerTypeAdapter(Uri::class.java, UriParser())
                        .create()
val listType: Type = object : TypeToken<ArrayList<RecyclingTrackerModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class RecyclingTrackerFileStore(val context: Context) : RecyclingTrackerStore {

    var recyclingTrackers = mutableListOf<RecyclingTrackerModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): List<RecyclingTrackerModel> {
        return recyclingTrackers
    }

    override fun findById(id: Long): RecyclingTrackerModel? {
        return recyclingTrackers.find { it.id == id }
    }

    override fun create(recyclingTracker: RecyclingTrackerModel) {
        recyclingTracker.id = generateRandomId()
        recyclingTrackers.add(recyclingTracker)
        serialize()
    }

    override fun update(recyclingTracker: RecyclingTrackerModel) {
        val recyclingTrackersList = findAll() as ArrayList<RecyclingTrackerModel>
        val found = recyclingTrackers.find { it.id == recyclingTracker.id }
        if (found != null) {
            found.title = recyclingTracker.title
            found.description = recyclingTracker.description
            found.date = recyclingTracker.date
            found.image = recyclingTracker.image
            found.lat = recyclingTracker.lat
            found.lng = recyclingTracker.lng
            found.zoom = recyclingTracker.zoom
            logAll()
        }
        serialize()
    }

    override fun delete(recyclingTracker: RecyclingTrackerModel) {
        val found = recyclingTrackers.find { it.id == recyclingTracker.id }
        if (found != null) {
            recyclingTrackers.remove(found)
            serialize()
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(recyclingTrackers, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        recyclingTrackers = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        recyclingTrackers.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
