package crabster.rudakov.requestsmediacontent.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.databinding.ItemMediaBinding

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    var currentList: List<MediaData> = emptyList()

    fun submitList(list: List<MediaData>) {
        if (currentList.isEmpty()) {
            currentList = list
            notifyItemRangeInserted(0, currentList.size)
        } else {
            notifyItemRangeRemoved(0, currentList.size)
            currentList = list
            notifyItemRangeInserted(0, currentList.size)
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

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size

}