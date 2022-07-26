package crabster.rudakov.requestsmediacontent.repository

import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.data.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun items(mediaType: MediaType): Flow<List<MediaData>>

}