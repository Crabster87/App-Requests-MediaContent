package crabster.rudakov.requestsmediacontent.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.databinding.ItemMediaBinding
import crabster.rudakov.requestsmediacontent.utils.DiffUtils

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    val differ = AsyncListDiffer(this, DiffUtils.diffCallback)

    fun submitList(list: List<MediaData>) {
        differ.submitList(list)
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
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

}