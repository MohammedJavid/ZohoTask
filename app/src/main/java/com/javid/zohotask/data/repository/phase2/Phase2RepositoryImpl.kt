package com.javid.zohotask.data.repository.phase2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.javid.zohotask.data.model.modelclass.paging.Result
import com.javid.zohotask.data.model.modelclass.weather.Error
import com.javid.zohotask.data.model.modelclass.weather.WeatherResponseModel
import com.javid.zohotask.data.remote.ZohoTaskApiService
import com.javid.zohotask.data.repository.paging.ZohoTaskPagingSource
import com.javid.zohotask.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Phase2RepositoryImpl @Inject constructor(
    private val zohoTaskApiService: ZohoTaskApiService
) : Phase2Repository {

    private var _weatherData = MutableLiveData<Resource<WeatherResponseModel>?>()
    val weatherData: LiveData<Resource<WeatherResponseModel>?>
    get() = _weatherData

    override suspend fun getWeatherData(url: String, city: String) {
        _weatherData.value = Resource.loading(null)
        val response = zohoTaskApiService.getWeatherData(url, city)

        if (response.isSuccessful) {
            _weatherData.value = Resource.success(response.body())
        } else {
            if (response.code() == 400) {

                val error = Gson().fromJson(
                    response.errorBody()!!.charStream(),
                    Error::class.java
                )
                _weatherData.value = Resource.error(error.message ?: "No matching location found", response.code(), null)
            } else {
                _weatherData.value = Resource.error(response.message(), response.code(), null)
            }
        }
    }

    override fun getResult(): Flow<PagingData<Result>> {
        return Pager(
            PagingConfig(pageSize = 25)
        ) {
            ZohoTaskPagingSource(zohoTaskApiService)
        }.flow
    }
}