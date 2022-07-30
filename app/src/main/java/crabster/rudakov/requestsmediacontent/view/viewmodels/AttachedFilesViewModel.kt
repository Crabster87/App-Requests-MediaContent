package crabster.rudakov.requestsmediacontent.view.viewmodels

import androidx.annotation.IdRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.repository.AttachedFilesRepository
import crabster.rudakov.requestsmediacontent.view.fragments.AttachedFilesFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AttachedFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val attachedFilesRepository: AttachedFilesRepository
) : ViewModel() {

    private var _items: MutableStateFlow<List<MediaData>> = attachedFilesRepository.items
    val items: StateFlow<List<MediaData>> = _items

    private val args = AttachedFilesFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val mediaData = args.mediaData

    init {
        _items.value = attachedFilesRepository.items(mediaData)
    }

    fun submitArgument(@IdRes id: Int) = attachedFilesRepository.args(id)

}