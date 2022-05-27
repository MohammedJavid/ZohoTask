package com.javid.zohotask.data.remote

import com.javid.zohotask.data.model.modelclass.paging.ResponseModel
import com.javid.zohotask.data.model.modelclass.weather.WeatherResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ZohoTaskApiService {

    @GET("?")
    suspend fun getData(@Query("page") page: Int, @Query("results") results: Int): Response<ResponseModel>

    @GET
    suspend fun getWeatherData(@Url url:String, @Query("q") city: String): Response<WeatherResponseModel>

}