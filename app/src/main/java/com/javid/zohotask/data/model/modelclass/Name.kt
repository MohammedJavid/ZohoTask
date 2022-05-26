package com.javid.zohotask.data.model.modelclass


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("first")
    @ColumnInfo(name = "first")
    val first: String?,
    @SerializedName("last")
    @ColumnInfo(name = "last")
    val last: String?,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?
)