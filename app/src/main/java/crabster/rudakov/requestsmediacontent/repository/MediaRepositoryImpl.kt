package crabster.rudakov.requestsmediacontent.repository

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.data.MediaData.Companion.WEEK_MINUTES
import crabster.rudakov.requestsmediacontent.data.MediaData.Companion.isValidTime
import crabster.rudakov.requestsmediacontent.data.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MediaRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val ioDispatcher: CoroutineContext
) : MediaRepository {

    override fun items(mediaType: MediaType): Flow<List<MediaData>> {
        val listMediaData: MutableList<MediaData> = mutableListOf()
        return flow {
            when (mediaType) {
                MediaType.PHOTO -> {
                    getPhotos().collectLatest { listMediaData.add(it) }
                    emit(listMediaData)
                }
                MediaType.VIDEO -> {
                    getVideo().collectLatest { listMediaData.add(it) }
                    emit(listMediaData)
                }
            }
        }
    }

    private fun getVideo(): Flow<MediaData> {
        return flow {
            val videoProjection = createProjection(MediaType.VIDEO)
            val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val videoCursor = createCursor(videoQueryUri, videoProjection)

            if (videoCursor != null && videoCursor.count > 0) {
                if (videoCursor.moveToFirst()) {
                    val idColumn = videoCursor.getColumnIndex(MediaStore.Video.Media._ID)
                    val dataColumn = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA)
                    val dateAddedColumn =
                        videoCursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)
                    val durationColumn =
                        videoCursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                    do {
                        val id = videoCursor.getString(idColumn)
                        val data = videoCursor.getString(dataColumn)
                        val dateAdded = videoCursor.getString(dateAddedColumn)
                        val isValidTime =
                            dateAdded.toLongOrNull()?.times(1000).isValidTime(WEEK_MINUTES)
                        if (isValidTime) {
                            val duration = videoCursor.getInt(durationColumn)
                            val galleryData = MediaData()
                            galleryData.also {
                                it.url = data
                                it.id = Integer.valueOf(id)
                                it.duration = duration
                                it.mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                                it.dateAdded = dateAdded
                                it.dateTimeOriginal = dateAdded.toLongOrNull()?.times(1000)
                            }
                            emit(galleryData)
                        }
                    } while (videoCursor.moveToNext())
                }
                videoCursor.close()
            }
        }
    }

    private fun getPhotos(): Flow<MediaData> {
        return flow {
            val imagesProjection = createProjection(MediaType.PHOTO)
            val imagesQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val imagesCursor = createCursor(imagesQueryUri, imagesProjection)

            if (imagesCursor != null && imagesCursor.count > 0) {
                if (imagesCursor.moveToFirst()) {
                    val idColumn = imagesCursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val dataColumn = imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val dateAddedColumn =
                        imagesCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    do {
                        val id = imagesCursor.getString(idColumn)
                        val data = imagesCursor.getString(dataColumn)
                        val dateAdded = imagesCursor.getString(dateAddedColumn)
                        val isValidTime =
                            dateAdded.toLongOrNull()?.times(1000).isValidTime(WEEK_MINUTES)
                        if (isValidTime) {
                            val galleryData = MediaData()
//                        val exif = PhotoHelper.extractExif(data)
                            galleryData.also {
//                            it.hasGeoTag = exif.hasGeoTag
                                it.dateTimeOriginal = dateAdded.toLongOrNull()?.times(1000)
//                            it.isScreenShot = exif.isScreenShot
                                it.url = data
                                it.id = Integer.valueOf(id)
                                it.mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                                it.dateAdded = dateAdded
//                            it.latitude = exif.latitude
//                            it.longitude = exif.longitude
                            }
                            emit(galleryData)
                        }
                    } while (imagesCursor.moveToNext())
                }
                imagesCursor.close()
            }
        }
    }

    private fun createProjection(mediaType: MediaType): Array<String> {
        return when (mediaType) {
            MediaType.VIDEO -> arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION
            )
            MediaType.PHOTO -> arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED
            )
        }
    }

    private fun createCursor(contentUri: Uri, projection: Array<String>): Cursor? {
        return contentResolver.query(
            contentUri,
            projection,
            null,
            null,
            "date_added DESC"
        )
    }

}