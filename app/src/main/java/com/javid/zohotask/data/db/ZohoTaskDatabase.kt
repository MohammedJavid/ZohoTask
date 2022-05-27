package com.javid.zohotask.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.javid.zohotask.data.model.modelclass.paging.Result
import com.javid.zohotask.data.model.typeconverter.*

@Database(entities = [Result::class],version = 1, exportSchema = false)
@TypeConverters(RegisteredModelConverter::class, NameModelConverter::class,
    PictureModelConverter::class, DobModelConverter::class, IdModelConverter::class,
    LocationModelConverter::class, LoginModelConverter::class)
abstract class ZohoTaskDatabase: RoomDatabase() {

    abstract fun getZohoTaskDao(): ZohoTaskDao

}