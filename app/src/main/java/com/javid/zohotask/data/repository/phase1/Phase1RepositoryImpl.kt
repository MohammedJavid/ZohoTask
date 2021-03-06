package com.javid.zohotask.data.repository.phase1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.javid.zohotask.data.db.ZohoTaskDao
import com.javid.zohotask.data.model.modelclass.paging.Result
import com.javid.zohotask.data.remote.ZohoTaskApiService
import com.javid.zohotask.data.repository.paging.ZohoTaskPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Phase1RepositoryImpl @Inject constructor(
    private val zohoTaskApiService: ZohoTaskApiService,
    private val zohoTaskDao: ZohoTaskDao) : Phase1Repository {

    private var _localData = MutableLiveData<List<Result>>()
    val localData: LiveData<List<Result>>
    get() = _localData

    override fun getResult(): Flow<PagingData<Result>> {
        return Pager(
            PagingConfig(pageSize = 10)) {
            ZohoTaskPagingSource(zohoTaskApiService)
        }.flow
    }

    override suspend fun addToDatabase(result: Result) {
        zohoTaskDao.insert(result)
    }

    override fun getAllData(): LiveData<List<Result>> {
        return zohoTaskDao.getAllData()
    }

    override suspend fun deleteAll() {
        zohoTaskDao.deleteAll()
    }


}