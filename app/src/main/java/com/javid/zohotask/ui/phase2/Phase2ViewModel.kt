package com.javid.zohotask.ui.phase2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.javid.zohotask.MainActivity
import com.javid.zohotask.data.model.modelclass.paging.Result
import com.javid.zohotask.data.repository.phase2.Phase2RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Phase2ViewModel @Inject constructor(private val phase2RepositoryImpl: Phase2RepositoryImpl): ViewModel() {

    val weatherData = phase2RepositoryImpl.weatherData
    var pagingResult: Flow<PagingData<Result>>? = null

    init {
        if (MainActivity.hasInternet) {
            pagingResult = phase2RepositoryImpl.getResult().cachedIn(viewModelScope)
        }
    }
    fun getWeatherData(url: String, city: String) {
        viewModelScope.launch {
            phase2RepositoryImpl.getWeatherData(url, city)
        }
    }
}