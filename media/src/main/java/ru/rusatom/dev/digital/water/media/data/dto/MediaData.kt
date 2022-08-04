package ru.rusatom.dev.digital.water.media.data.dto

import android.location.Location
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.rusatom.dev.digital.water.media.utils.BuildConfig

@Parcelize
data class MediaData(
    var id: Int = 0,
    var url: String = "",
    var name: String = "",
    var albumId: Int = 0,
    var isSelected: Boolean = false,
    var mediaType: MediaType = MediaType.PHOTO,
    var duration: Int = 0,
    var durationTime: String = "",
    var dateAdded: String = "",
    var dateTimeOriginal: Long? = null,
    var hasGeoTag: Boolean = false,
    var isScreenShot: Boolean = false,
    var latitude: Double? = null,
    var longitude: Double? = null,
) : Parcelable {

    companion object {
        const val DAY_MINUTES = 1440
        const val WEEK_MINUTES = 7 * 1440
        const val ONE_MINUTE_IN_MILLIS = 60000L
        fun Long?.isValidTime(minutes: Int = BuildConfig.MAX_AGE_MINUTES): Boolean {
            this ?: return false
            val today = System.currentTimeMillis()
            val diff = (today - this) / ONE_MINUTE_IN_MILLIS
            return diff <= minutes
        }
    }

    val isValidTime: Boolean
        get() = dateTimeOriginal.isValidTime()

    val isVideo: Boolean
        get() = mediaType == MediaType.VIDEO

    fun isValidLocation(userLocation: Location?): Boolean {
        if (userLocation == null || latitude == null || longitude == null) return false
        val currentLocation = Location("").also {
            it.latitude = latitude!!
            it.longitude = longitude!!
        }
        val distance = userLocation.distanceTo(currentLocation).toInt()
        return distance <= BuildConfig.MAX_PHOTO_DISTANCE
    }
}