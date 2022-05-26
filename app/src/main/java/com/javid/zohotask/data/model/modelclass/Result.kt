package com.javid.zohotask.data.model.modelclass

import androidx.room.*
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "ZohoTask", indices = [Index(value = ["email"],unique = true)])
data class Result(
    @SerializedName("cell")
    @ColumnInfo(name = "cell")
    val cell: String?,
    @SerializedName("dob")
    @ColumnInfo(name = "dob")
    val dob: Dob?,
    @PrimaryKey
    @NotNull
    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String,
    @SerializedName("gender")
    @ColumnInfo(name = "gender")
    val gender: String?,
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Id?,
    @SerializedName("location")
    @ColumnInfo(name = "location")
    val location: Location?,
    @SerializedName("login")
    @ColumnInfo(name = "login")
    val login: Login?,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: Name?,
    @SerializedName("nat")
    @ColumnInfo(name = "nat")
    val nat: String?,
    @SerializedName("phone")
    @ColumnInfo(name = "phone")
    val phone: String?,
    @SerializedName("picture")
    @ColumnInfo(name = "picture")
    val picture: Picture?,
    @SerializedName("registered")
    @ColumnInfo(name = "registered")
    val registered: Registered
)

