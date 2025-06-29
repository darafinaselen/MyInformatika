package com.example.myinformatika

import android.os.Parcel
import android.os.Parcelable

data class InfoItem(
    val category: String,
    val title: String,
    val description: String,
    val date: String,
    val views: String,
    val fileName: String? = null,
    val fileSize: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        category = parcel.readString()!!,
        title = parcel.readString()!!,
        description = parcel.readString()!!,
        date = parcel.readString()!!,
        views = parcel.readString()!!,
        fileName = parcel.readString(),
        fileSize = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(views)
        parcel.writeString(fileName)
        parcel.writeString(fileSize)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<InfoItem> {
        override fun createFromParcel(parcel: Parcel): InfoItem {
            return InfoItem(parcel)
        }

        override fun newArray(size: Int): Array<InfoItem?> {
            return arrayOfNulls(size)
        }
    }
}