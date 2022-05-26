package com.javid.zohotask.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.javid.zohotask.R
import com.javid.zohotask.ZohoTaskApplication
import com.javid.zohotask.data.db.ZohoTaskDao
import com.javid.zohotask.data.db.ZohoTaskDatabase
import com.javid.zohotask.data.remote.ZohoTaskApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    internal fun provideContext(): Context {
        return ZohoTaskApplication.instance!!.applicationContext
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(provideContext().resources.getString(R.string.base_url))
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application, roomCallback: RoomDatabase.Callback): ZohoTaskDatabase {
        return Room.databaseBuilder(application.applicationContext,
            ZohoTaskDatabase::class.java,
            "ZohoTask")
            .fallbackToDestructiveMigration()
            .addCallback(roomCallback)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideRoomDatabaseCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }


    @Provides
    @Singleton
    fun provideZohoTaskApiService(retrofit: Retrofit): ZohoTaskApiService {
        return retrofit.create(ZohoTaskApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideZohoTaskDao(zohoTaskDatabase: ZohoTaskDatabase): ZohoTaskDao {
        return zohoTaskDatabase.getZohoTaskDao()
    }

}