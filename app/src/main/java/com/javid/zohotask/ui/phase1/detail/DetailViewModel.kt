package com.javid.zohotask.ui.phase1.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.javid.zohotask.data.model.modelclass.paging.Result
import com.javid.zohotask.data.repository.detail.DetailRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepositoryImpl: DetailRepositoryImpl
) : ViewModel() {

    suspend fun getDataByEmail(email: String): LiveData<Result>{
        return withContext(Dispatchers.IO) {
            detailRepositoryImpl.getDataByEmail(email)
        }
    }
}