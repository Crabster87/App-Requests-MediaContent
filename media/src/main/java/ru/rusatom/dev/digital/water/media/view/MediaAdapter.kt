package ru.rusatom.dev.digital.water.media.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.rusatom.dev.digital.water.media.data.dto.MediaData
import ru.rusatom.dev.digital.water.media.databinding.ItemMediaBinding
import ru.rusatom.dev.digital.water.media.utils.DiffUtils

class MediaAdapter(val function: ((MediaData) -> Unit)? = null) :
    ListAdapter<MediaData, MediaAdapter.ViewHolder>(DiffUtils.diffCallback) {

    private var emptyList = false

    override fun submitList(list: List<MediaData>?) {
        emptyList = list.isNullOrEmpty()
        super.submitList(list)
        if (emptyList) {
            notifyItemChanged(0)
        } else {
            notifyItemRangeChanged(0, currentList.size)
        }
    }

    inner class ViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MediaData) {
            binding.apply {
                image.clipToOutline = true
                Glide.with(itemView.context)
                    .load(item.url)
                    .into(image)
            }
            itemView.setOnClickListener {
                function?.invoke(item)
            }
            if (item.isVideo) {
                binding.apply {
                    videoDuration.visibility = View.VISIBLE
                    videoDuration.text = item.durationTime
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}