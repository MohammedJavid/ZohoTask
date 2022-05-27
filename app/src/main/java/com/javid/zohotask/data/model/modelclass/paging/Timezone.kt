package com.javid.zohotask.data.model.modelclass.paging


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Timezone(
    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String?,
    @SerializedName("offset")
    @ColumnInfo(name = "offset")
    val offset: String?
)