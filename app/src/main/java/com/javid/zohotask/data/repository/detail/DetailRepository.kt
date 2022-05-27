package com.javid.zohotask.data.repository.detail

import androidx.lifecycle.LiveData
import com.javid.zohotask.data.model.modelclass.paging.Result

interface DetailRepository {

    fun getDataByEmail(email: String): LiveData<Result>

}