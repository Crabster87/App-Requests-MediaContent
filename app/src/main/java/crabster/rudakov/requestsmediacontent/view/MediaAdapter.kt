package crabster.rudakov.requestsmediacontent.view

import android.content.Context
import android.location.Location
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.utils.BuildConfig
import crabster.rudakov.requestsmediacontent.utils.DateUtil
import crabster.rudakov.requestsmediacontent.utils.DiffUtils

class MediaAdapter(
    private val context: Context
) : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    private val totalHours = BuildConfig.MAX_AGE_MINUTES / 60
    private var currentLocation: Location? = null

    fun submitList(items: List<MediaData>?) = differ.submitList(items)

    fun currentList(): List<MediaData> {
        return differ.currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false)
        )
    }

    override fun onBindViewHolder(h: ViewHolder, position: Int) {
        val m = currentList()[position]
        m.isSelected = false
        h.checkbox.background = ContextCompat.getDrawable(context, R.drawable.ic_round)
        Glide.with(context)
            .load(m.url)
            .into(h.image)

        var isPhoto = true
        if (m.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            isPhoto = false
            h.durationFrame.visibility = View.VISIBLE
            h.durationLabel.text = DateUtil().millisToTime(m.duration.toLong())
        }

        h.itemView.setOnClickListener {
            if (m.isScreenShot && isPhoto && BuildConfig.BLOCK_SCREENSHOT) {
                it.showToast("Неподходящий формат.")
                return@setOnClickListener
            }
            if (!m.isValidTime && BuildConfig.BLOCK_AGE) {
                it.showToast("Съемка сделана более $totalHours часов назад.")
                return@setOnClickListener
            }
            if (!m.hasGeoTag && isPhoto && BuildConfig.BLOCK_GEOTAG) {
                it.showToast("Отсутствуют геотеги. Проверьте настройки камеры")
                return@setOnClickListener
            }
            if (currentLocation == null && BuildConfig.BLOCK_GEOTAG) {
                it.showToast("Не удалось определить текущую локацию.Повторите попытку позднее")
                return@setOnClickListener
            }
            if (!m.isValidLocation(currentLocation) && isPhoto && BuildConfig.BLOCK_GEOTAG) {
                it.showToast("Удаленность от объекта превышает ${BuildConfig.MAX_PHOTO_DISTANCE} метров")
                return@setOnClickListener
            }

            currentList().forEachIndexed { i, mediaData ->
                if (mediaData.isSelected) {
                    notifyItemChanged(i)
                }
            }
            m.isSelected = !m.isSelected
            if (m.isSelected) {
                h.checkbox.background = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
            } else {
                h.checkbox.background = ContextCompat.getDrawable(context, R.drawable.ic_round)
            }
        }
    }

    private fun View.showToast(message: String, isLong: Boolean = true) {
        val duration = if (isLong) {
            Toast.LENGTH_LONG
        } else {
            Toast.LENGTH_SHORT
        }
        Toast.makeText(
            context,
            message,
            duration
        ).show()
    }

    override fun getItemCount(): Int {
        return currentList().size
    }

    fun getCheckItems(): List<MediaData> {
        return currentList().filter { mediaData -> mediaData.isSelected }
    }

    fun submitLocation(location: Location?) {
        currentLocation = location
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val durationFrame: FrameLayout = view.findViewById(R.id.durationFrame)
        val durationLabel: TextView = view.findViewById(R.id.durationLabel)
        val checkbox: ImageView = view.findViewById(R.id.checkbox)
    }

    private val differ = AsyncListDiffer(this, DiffUtils.diffCallback)

}