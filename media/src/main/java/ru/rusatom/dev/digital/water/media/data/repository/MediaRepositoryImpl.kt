package ru.rusatom.dev.digital.water.media.data.repository

import android.app.Application
import android.database.Cursor
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import ru.rusatom.dev.digital.water.media.data.dto.MediaData
import ru.rusatom.dev.digital.water.media.data.dto.MediaType
import ru.rusatom.dev.digital.water.media.repository.MediaRepository
import ru.rusatom.dev.digital.water.media.utils.DateUtil
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class MediaRepositoryImpl @Inject constructor(
    private val application: Application,
    private val ioDispatcher: CoroutineContext
) : MediaRepository {

    override fun items(): Flow<List<MediaData>> {
        val listMediaData: MutableList<MediaData> = mutableListOf()
        return flow {
            getMedia().collectLatest { listMediaData.add(it) }
            emit(listMediaData)
        }
    }

    private fun getMedia(): Flow<MediaData> {
        return flow {
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Video.Media.DURATION
            )
            val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

            val queryUri = MediaStore.Files.getContentUri("external")
            val cursorLoader = CursorLoader(
                application,
                queryUri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            )
            val cursor: Cursor? = cursorLoader.loadInBackground()
            if (cursor != null && cursor.count > 0) {
                if (cursor.moveToFirst()) {
                    val idColumn =
                        cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                    val dataColumn =
                        cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                    val dateAddedColumn =
                        cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                    val mediaTypeColumn =
                        cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                    val durationColumn =
                        cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                    do {
                        val id = cursor.getString(idColumn)
                        val data = cursor.getString(dataColumn)
                        val dateAdded = cursor.getString(dateAddedColumn)
                        val mediaType = cursor.getString(mediaTypeColumn)
                        val duration = cursor.getInt(durationColumn)
                        val galleryData = MediaData()
                        galleryData.also {
                            it.url = data
                            it.id = Integer.valueOf(id)
                            it.duration = duration
                            it.durationTime = DateUtil().millisToTime(duration.toLong())
                            it.mediaType = checkMediaType(mediaType)
                            it.dateAdded = dateAdded
                            it.dateTimeOriginal = dateAdded.toLongOrNull()?.times(1000)
                        }
                        emit(galleryData)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        }
    }

    private fun checkMediaType(mediaType: String): MediaType {
        return when (mediaType) {
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString() -> MediaType.VIDEO
            else -> MediaType.PHOTO
        }
    }

}