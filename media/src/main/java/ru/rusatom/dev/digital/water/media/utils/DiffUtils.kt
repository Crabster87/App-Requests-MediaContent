package ru.rusatom.dev.digital.water.media.utils

import androidx.recyclerview.widget.DiffUtil
import ru.rusatom.dev.digital.water.media.data.dto.MediaData

object DiffUtils {

    val diffCallback = object : DiffUtil.ItemCallback<MediaData>() {
        override fun areItemsTheSame(oldItem: MediaData, newItem: MediaData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaData, newItem: MediaData): Boolean {
            return oldItem.id == newItem.id && oldItem.url == newItem.url
        }
    }

}