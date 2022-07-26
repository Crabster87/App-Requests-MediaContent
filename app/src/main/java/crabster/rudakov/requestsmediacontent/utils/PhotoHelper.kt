package crabster.rudakov.requestsmediacontent.utils

import android.location.Location
import android.media.ExifInterface
import java.text.SimpleDateFormat
import java.util.*

object PhotoHelper {

    fun saveGeoTag(location: Location?, imagePath: String?): Boolean {
        if (location == null || imagePath == null) return false
        return try {
            val exif = ExifInterface(imagePath)
//            exif.setGpsInfo(location)
            exif.saveAttributes()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun hasGeoTag(imagePath: String?): Boolean {
        imagePath ?: return false
        return try {
            val exif = ExifInterface(imagePath)
            val lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            val lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            return lat != null && lon != null
        } catch (e: Exception) {
            false
        }
    }

//    fun extractExif(imagePath: String?): PhotoExif {
//        val data = PhotoExif()
//        imagePath ?: return data
//        try {
//            val dateFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
//            ExifInterface(imagePath).apply {
//                getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)?.let {
//                    data.dateTimeOriginal = dateFormat.parse(it)?.time
//                }
//                getAttribute(ExifInterface.TAG_DATETIME)?.let {
//                    data.dateTime = dateFormat.parse(it)?.time
//                    data.dateAdded = it
//                    data.id = data.dateTime?.toInt() ?: 0
//                }
//
//                getAttribute(ExifInterface.TAG_ORIENTATION)?.let {
//                    data.orientation = it.toIntOrNull()
//                }
//                getAttribute(ExifInterface.TAG_CUSTOM_RENDERED)?.let {
//                    data.customRendered = it.toIntOrNull()
//                }
//                getAttribute(ExifInterface.TAG_MODEL)?.let {
//                    data.model = it
//                }
//                latLong?.let {
//                    data.latitude = it[0]
//                    data.longitude = it[1]
//                }
//                getAttribute(ExifInterface.TAG_GPS_LONGITUDE)?.let {
//                    data.longitudeExif = it
//                }
//                getAttribute(ExifInterface.TAG_GPS_LATITUDE)?.let {
//                    data.latitudeExif = it
//                }
//            }
//        } catch (ex: Exception) {
//            data.errorMessage = ex.message
//        }
//        return data
//    }
}