package com.javid.zohotask.data.model.modelclass


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("large")
    @ColumnInfo(name = "large")
    val large: String?,
    @SerializedName("medium")
    @ColumnInfo(name = "medium")
    val medium: String?,
    @SerializedName("thumbnail")
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String?
)