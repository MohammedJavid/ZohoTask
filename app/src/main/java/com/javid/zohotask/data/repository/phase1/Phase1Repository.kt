package com.javid.zohotask.data.repository.phase1

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.javid.zohotask.data.model.modelclass.paging.Result
import kotlinx.coroutines.flow.Flow

interface Phase1Repository {
    fun getResult(): Flow<PagingData<Result>>
    suspend fun addToDatabase(result: Result)
    fun getAllData(): LiveData<List<Result>>
}