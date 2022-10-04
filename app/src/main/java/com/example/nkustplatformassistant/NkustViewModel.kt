package com.example.nkustplatformassistant

import androidx.lifecycle.*
import com.example.nkustplatformassistant.data.persistence.DataRepository
import kotlinx.coroutines.launch

class NkustViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun dataAvailability() = liveData {
            emit (dataRepository.checkDataIsReady())
    }
}