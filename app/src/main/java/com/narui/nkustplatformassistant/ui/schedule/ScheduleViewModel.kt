package com.narui.nkustplatformassistant.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScheduleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScheduleViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _schedules: MutableLiveData<List<ScheduleEntity>> = MutableLiveData()
    val schedules: LiveData<List<ScheduleEntity>> get() = _schedules


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _schedules.postValue(dataRepository.getScheduleFromDB())
        }
    }
}
