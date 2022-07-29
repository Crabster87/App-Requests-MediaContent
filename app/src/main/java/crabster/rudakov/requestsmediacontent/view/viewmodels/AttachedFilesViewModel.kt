package crabster.rudakov.requestsmediacontent.view.viewmodels

import android.util.Log
import androidx.annotation.IdRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.data.MediaType
import crabster.rudakov.requestsmediacontent.view.fragments.AttachedFilesFragmentArgs
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

    private val args = AttachedFilesFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val mediaData = args.mediaData

    lateinit var mediaType: MediaType
        private set

    init {
        _items.value = getMedia()
        Log.d("LIST SIZE IN VM", _items.value.size.toString())
        _items.value.forEach { Log.d("LIST ITEM IN VM", it.url) }
    }

    fun submitArgument(@IdRes id: Int) {
        mediaType = when (id) {
            R.id.action_attach_photo -> MediaType.PHOTO
            else -> MediaType.VIDEO
        }
    }

    private fun getMedia(): List<MediaData> {
        return when (mediaData) {
            null -> _items.value
            else -> _items.value + mediaData
        }
    }

}