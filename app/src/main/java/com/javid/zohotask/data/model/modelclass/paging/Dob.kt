package com.javid.zohotask.data.model.modelclass.paging


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Dob(
    @SerializedName("age")
    @ColumnInfo(name = "age")
    val age: Int?,
    @SerializedName("date")
    @ColumnInfo(name = "date")
    val date: String?
)