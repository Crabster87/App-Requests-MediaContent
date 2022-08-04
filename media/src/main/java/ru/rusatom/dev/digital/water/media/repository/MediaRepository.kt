package ru.rusatom.dev.digital.water.media.repository

import kotlinx.coroutines.flow.Flow
import ru.rusatom.dev.digital.water.media.data.dto.MediaData
import ru.rusatom.dev.digital.water.media.data.dto.MediaType

interface MediaRepository {

    fun items(mediaType: MediaType): Flow<List<MediaData>>

}