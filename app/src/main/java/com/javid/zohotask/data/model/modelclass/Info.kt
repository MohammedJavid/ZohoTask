package com.javid.zohotask.data.model.modelclass


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("page")
    @ColumnInfo(name = "page")
    val page: Int?,
    @SerializedName("results")
    @ColumnInfo(name = "results")
    val results: Int?,
    @SerializedName("seed")
    @ColumnInfo(name = "seed")
    val seed: String?,
    @SerializedName("version")
    @ColumnInfo(name = "version")
    val version: String?
)