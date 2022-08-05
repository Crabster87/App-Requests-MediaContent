package ru.rusatom.dev.digital.water.media.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.rusatom.dev.digital.water.media.data.dto.MediaData
import ru.rusatom.dev.digital.water.media.data.dto.MediaType
import ru.rusatom.dev.digital.water.media.repository.MediaRepository
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private var _items: MutableStateFlow<List<MediaData>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<MediaData>> = _items

    fun getMedia(mediaType: MediaType?) {
        viewModelScope.launch {
            mediaRepository.items().collectLatest { list ->
                when (mediaType) {
                    MediaType.PHOTO -> _items.value =
                        list.filter { it.mediaType == MediaType.PHOTO }
                    MediaType.VIDEO -> _items.value =
                        list.filter { it.mediaType == MediaType.VIDEO }
                    null -> _items.value = list
                }
            }
        }
    }

}