package crabster.rudakov.requestsmediacontent.view.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import crabster.rudakov.requestsmediacontent.data.MediaData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AttachedFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _items: MutableStateFlow<List<MediaData>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<MediaData>> = _items

    fun submitMediaData(mediaData: MediaData) {
        _items.value = _items.value.plus(mediaData).distinct()
    }

}