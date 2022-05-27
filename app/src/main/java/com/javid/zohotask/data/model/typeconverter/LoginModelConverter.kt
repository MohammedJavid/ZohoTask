package com.javid.zohotask.data.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.javid.zohotask.data.model.modelclass.paging.Login

class LoginModelConverter {

    @TypeConverter
    fun fromString(value: String): Login = Gson().fromJson(value, object : TypeToken<Login>() {}.type)

    @TypeConverter
    fun fromModel(value: Login): String = Gson().toJson(value)

}