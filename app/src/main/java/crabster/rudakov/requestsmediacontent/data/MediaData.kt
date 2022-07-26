package crabster.rudakov.requestsmediacontent.data

import android.location.Location
import android.os.Parcelable
import android.provider.MediaStore
import crabster.rudakov.requestsmediacontent.utils.BuildConfig
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaData(
    var id: Int = 0,
    var url: String = "",
    var name: String = "",
    var albumId: Int = 0,
    var isSelected: Boolean = false,
    var mediaType: Int = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
    var duration: Int = 0,
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