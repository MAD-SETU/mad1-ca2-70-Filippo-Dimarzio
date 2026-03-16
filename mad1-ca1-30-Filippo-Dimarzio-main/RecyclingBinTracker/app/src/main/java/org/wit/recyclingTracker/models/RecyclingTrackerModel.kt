package org.wit.recyclingTracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecyclingTrackerModel(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var date: String = ""
) : Parcelable
