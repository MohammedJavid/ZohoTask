package com.javid.zohotask.data.model.modelclass


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?,
    @SerializedName("value")
    @ColumnInfo(name = "value")
    val value: String?
)