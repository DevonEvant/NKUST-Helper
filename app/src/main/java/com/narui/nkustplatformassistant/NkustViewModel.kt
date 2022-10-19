package com.narui.nkustplatformassistant

import androidx.lifecycle.*
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class NkustViewModel(private val dataRepository: DataRepository) : ViewModel() {
    init {
        // checkDbState
        viewModelScope.launch(Dispatchers.IO) {
            dbDataAvailability.postValue(dataRepository.checkDataIsReady())
        }
    }
}