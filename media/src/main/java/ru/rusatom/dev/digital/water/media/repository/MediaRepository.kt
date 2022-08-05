package ru.rusatom.dev.digital.water.media.repository

import kotlinx.coroutines.flow.Flow
import ru.rusatom.dev.digital.water.media.data.dto.MediaData

interface MediaRepository {

    fun items(): Flow<List<MediaData>>

}