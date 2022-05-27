package com.javid.zohotask.data.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javid.zohotask.data.model.modelclass.paging.Name

class NameModelConverter {
    @TypeConverter
    fun fromString(value: String): Name = Gson().fromJson(value, object : TypeToken<Name>() {}.type)

    @TypeConverter
    fun fromModel(value: Name): String = Gson().toJson(value)
}