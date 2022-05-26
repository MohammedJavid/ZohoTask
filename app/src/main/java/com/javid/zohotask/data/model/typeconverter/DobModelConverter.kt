package com.javid.zohotask.data.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javid.zohotask.data.model.modelclass.Dob

class DobModelConverter {
    @TypeConverter
    fun fromString(value: String): Dob = Gson().fromJson(value, object : TypeToken<Dob>() {}.type)

    @TypeConverter
    fun fromModel(value: Dob): String = Gson().toJson(value)
}