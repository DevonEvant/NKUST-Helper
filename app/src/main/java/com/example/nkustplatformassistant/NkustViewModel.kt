package com.example.nkustplatformassistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.persistence.DataRepository
import kotlinx.coroutines.launch

class NkustViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _dataAvailability = MutableLiveData(false)
    val dataAvailability: LiveData<Boolean> = _dataAvailability

    init {
        viewModelScope.launch {
            _dataAvailability.value = checkDataAvailability()
        }
    }

    private suspend fun checkDataAvailability(): Boolean {
        var availability = false
        viewModelScope.launch {
            availability = dataRepository.checkDataIsReady()
        }
        return availability
    }
}