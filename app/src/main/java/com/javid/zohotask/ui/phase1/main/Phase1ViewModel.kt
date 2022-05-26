package com.javid.zohotask.ui.phase1.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.javid.zohotask.MainActivity
import com.javid.zohotask.data.model.modelclass.Result
import com.javid.zohotask.data.repository.Phase1RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Phase1ViewModel @Inject constructor(
    private val phase1RepositoryImpl: Phase1RepositoryImpl
) : ViewModel() {

    var pagingResult: Flow<PagingData<Result>>? = null
    val localData = phase1RepositoryImpl.getAllData()
    init {
        if (MainActivity.hasInternet) {
            pagingResult = phase1RepositoryImpl.getResult().cachedIn(viewModelScope)
        }
    }

    fun addToDatabase(result: Result) {
        viewModelScope.launch {
            phase1RepositoryImpl.addToDatabase(result)
        }
    }

}