package com.javid.zohotask.data.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javid.zohotask.data.model.modelclass.Registered

class RegisteredModelConverter {

    @TypeConverter
    fun fromString(value: String): Registered = Gson().fromJson(value, object : TypeToken<Registered>() {}.type)

    @TypeConverter
    fun fromModel(value: Registered): String = Gson().toJson(value)

}