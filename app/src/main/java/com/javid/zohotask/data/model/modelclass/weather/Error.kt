package com.javid.zohotask.data.model.modelclass.weather


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?
)