package ru.rusatom.dev.digital.water.media.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MediaType : Parcelable {
    VIDEO,
    PHOTO
}