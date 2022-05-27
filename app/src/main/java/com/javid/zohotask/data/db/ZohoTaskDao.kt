package com.javid.zohotask.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.javid.zohotask.data.model.modelclass.paging.Result

@Dao
interface ZohoTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: Result): Long

    @Query("SELECT * FROM ZohoTask WHERE email = :email")
    fun getDataByEmail(email: String): LiveData<Result>

    @Query("DELETE FROM ZohoTask")
    suspend fun deleteAll()

    @Query("SELECT * FROM ZohoTask")
    fun getAllData(): LiveData<List<Result>>

}