package com.javid.zohotask.data.model.modelclass


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city")
    @ColumnInfo(name = "city")
    val city: String?,
    @SerializedName("coordinates")
    @ColumnInfo(name = "coordinates")
    val coordinates: Coordinates?,
    @SerializedName("country")
    @ColumnInfo(name = "country")
    val country: String?,
    @SerializedName("postcode")
    @ColumnInfo(name = "postcode")
    val postcode: Any?,
    @SerializedName("state")
    @ColumnInfo(name = "state")
    val state: String?,
    @SerializedName("street")
    @ColumnInfo(name = "street")
    val street: Street?,
    @SerializedName("timezone")
    @ColumnInfo(name = "timezone")
    val timezone: Timezone?
)