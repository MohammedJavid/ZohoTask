package com.javid.zohotask.data.remote

import com.javid.zohotask.data.model.modelclass.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ZohoTaskApiService {

    @GET("?")
    suspend fun getData(@Query("page") page: Int, @Query("results") results: Int): Response<ResponseModel>

}