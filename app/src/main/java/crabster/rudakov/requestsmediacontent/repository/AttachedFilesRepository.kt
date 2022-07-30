package crabster.rudakov.requestsmediacontent.repository

import androidx.annotation.IdRes
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.data.MediaType
import kotlinx.coroutines.flow.MutableStateFlow

interface AttachedFilesRepository {

    var items: MutableStateFlow<List<MediaData>>

    fun items(mediaData: MediaData?): List<MediaData>

    fun args(@IdRes id: Int): MediaType

}