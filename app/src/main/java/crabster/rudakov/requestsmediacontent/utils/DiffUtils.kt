package crabster.rudakov.requestsmediacontent.utils

import androidx.recyclerview.widget.DiffUtil
import crabster.rudakov.requestsmediacontent.data.MediaData

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