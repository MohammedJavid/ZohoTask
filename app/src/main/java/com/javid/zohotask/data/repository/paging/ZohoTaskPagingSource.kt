package com.javid.zohotask.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.javid.zohotask.data.model.modelclass.paging.Result
import com.javid.zohotask.data.remote.ZohoTaskApiService
import retrofit2.HttpException
import java.io.IOException

class ZohoTaskPagingSource(
    private val zohoTaskApiService: ZohoTaskApiService
) : PagingSource<Int, Result>() {

    private var page = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val position = params.key ?: page
            val response = zohoTaskApiService.getData(position, 25)
            val data = response.body()?.results ?: emptyList()
            val responseData = mutableListOf<Result>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if(position == page) null else position - 1,
                nextKey = if(responseData.isEmpty()) null else position + 1
            )

        } catch (error: IOException) {
            LoadResult.Error(error)
        }catch (error: HttpException) {
            LoadResult.Error(error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}