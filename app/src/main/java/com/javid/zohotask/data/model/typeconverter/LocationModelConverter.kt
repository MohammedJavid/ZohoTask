package com.javid.zohotask.data.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javid.zohotask.data.model.modelclass.Location

class LocationModelConverter {

    @TypeConverter
    fun fromString(value: String): Location = Gson().fromJson(value, object : TypeToken<Location>() {}.type)

    @TypeConverter
    fun fromModel(value: Location): String = Gson().toJson(value)
}