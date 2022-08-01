package crabster.rudakov.requestsmediacontent.repository

import androidx.annotation.IdRes
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.data.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AttachedFilesRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineContext
) : AttachedFilesRepository {

    override var items: MutableStateFlow<List<MediaData>> = MutableStateFlow(emptyList())

    override fun items(mediaData: MediaData?): List<MediaData> {
        return when (mediaData) {
            null -> items.value
            else -> items.value + mediaData
        }.distinct()
    }

    override fun args(@IdRes id: Int): MediaType {
        return when (id) {
            R.id.action_attach_photo -> MediaType.PHOTO
            else -> MediaType.VIDEO
        }
    }

}