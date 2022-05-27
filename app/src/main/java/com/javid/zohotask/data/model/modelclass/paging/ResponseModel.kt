package com.javid.zohotask.data.model.modelclass.paging


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("info")
    @ColumnInfo(name = "info")
    val info: Info?,
    @SerializedName("results")
    @ColumnInfo(name = "results")
    val results: List<Result>?
)