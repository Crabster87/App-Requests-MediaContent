package crabster.rudakov.requestsmediacontent.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MediaType : Parcelable {
    VIDEO,
    PHOTO
}