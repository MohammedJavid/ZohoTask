package com.javid.zohotask.data.model.modelclass


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("md5")
    @ColumnInfo(name = "md5")
    val md5: String?,
    @SerializedName("password")
    @ColumnInfo(name = "password")
    val password: String?,
    @SerializedName("salt")
    @ColumnInfo(name = "salt")
    val salt: String?,
    @SerializedName("sha1")
    @ColumnInfo(name = "sha1")
    val sha1: String?,
    @SerializedName("sha256")
    @ColumnInfo(name = "sha256")
    val sha256: String?,
    @SerializedName("username")
    @ColumnInfo(name = "username")
    val username: String?,
    @SerializedName("uuid")
    @ColumnInfo(name = "uuid")
    val uuid: String?
)