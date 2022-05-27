package com.javid.zohotask.data.model.modelclass.weather


import com.google.gson.annotations.SerializedName

data class WeatherResponseModel(
    @SerializedName("current")
    val current: Current?,
    @SerializedName("location")
    val location: Location?
)