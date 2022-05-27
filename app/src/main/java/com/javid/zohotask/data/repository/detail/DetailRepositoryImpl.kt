package com.javid.zohotask.data.repository.detail

import androidx.lifecycle.LiveData
import com.javid.zohotask.data.db.ZohoTaskDao
import com.javid.zohotask.data.model.modelclass.paging.Result
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(private val zohoTaskDao: ZohoTaskDao): DetailRepository {

    override fun getDataByEmail(email: String): LiveData<Result> {
        return zohoTaskDao.getDataByEmail(email)
    }

}