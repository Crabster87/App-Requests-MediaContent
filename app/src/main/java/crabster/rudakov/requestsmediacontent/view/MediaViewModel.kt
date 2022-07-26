package crabster.rudakov.requestsmediacontent.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.repository.MediaRepository
import crabster.rudakov.requestsmediacontent.data.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private var _items: MutableStateFlow<List<MediaData>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<MediaData>> = _items

    fun getMedia(mediaType: MediaType) {
        viewModelScope.launch {
            mediaRepository.items(MediaType.PHOTO).collectLatest {
                _items.value = it
            }
        }
    }

}