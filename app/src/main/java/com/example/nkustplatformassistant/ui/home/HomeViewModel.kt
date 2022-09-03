package com.example.nkustplatformassistant.ui.home

import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.persistence.DataRepository

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _isDataReady: MutableLiveData<Boolean> = MutableLiveData()
    val isDataReady: LiveData<Boolean> = _isDataReady

    private fun checkDataState() {
        viewModelScope.launch {
            _isDataReady.value = dataRepository.checkDataIsReady()
        }
    }

    init {
        checkDataState()
    }
}
