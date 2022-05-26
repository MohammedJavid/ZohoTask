package com.javid.zohotask.ui.phase1.detail

import androidx.lifecycle.ViewModel
import com.javid.zohotask.data.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository
) : ViewModel() {
}