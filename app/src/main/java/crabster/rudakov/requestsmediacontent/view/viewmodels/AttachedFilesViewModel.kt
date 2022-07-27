package crabster.rudakov.requestsmediacontent.view.viewmodels

import androidx.annotation.IdRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.data.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AttachedFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    lateinit var argument: MediaType
        private set

    fun submitArgument(@IdRes id: Int) {
        argument = when (id) {
            R.id.action_attach_photo -> MediaType.PHOTO
            else -> MediaType.VIDEO
        }
    }

}