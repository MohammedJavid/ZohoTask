package com.javid.zohotask.data.repository.phase2

import androidx.paging.PagingData
import com.javid.zohotask.data.model.modelclass.paging.Result
import kotlinx.coroutines.flow.Flow

interface Phase2Repository {
    suspend fun getWeatherData(url: String, city:String)
    fun getResult(): Flow<PagingData<Result>>
}