package com.javid.zohotask.data.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javid.zohotask.data.model.modelclass.paging.Id

class IdModelConverter {

    @TypeConverter
    fun fromString(value: String): Id = Gson().fromJson(value, object : TypeToken<Id>() {}.type)

    @TypeConverter
    fun fromModel(value: Id): String = Gson().toJson(value)
}