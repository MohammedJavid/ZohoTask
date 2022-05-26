package com.javid.zohotask.data.model.modelclass


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("latitude")
    @ColumnInfo(name = "latitude")
    val latitude: String?,
    @SerializedName("longitude")
    @ColumnInfo(name = "longitude")
    val longitude: String?
)