package com.javid.zohotask.data.model.modelclass.paging


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Street(
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?,
    @SerializedName("number")
    @ColumnInfo(name = "number")
    val number: Int?
)