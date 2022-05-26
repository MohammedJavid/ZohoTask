package com.javid.zohotask.data.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javid.zohotask.data.model.modelclass.Picture


class PictureModelConverter {

    @TypeConverter
    fun fromString(value: String): Picture = Gson().fromJson(value, object : TypeToken<Picture>() {}.type)

    @TypeConverter
    fun fromModel(value: Picture): String = Gson().toJson(value)

}