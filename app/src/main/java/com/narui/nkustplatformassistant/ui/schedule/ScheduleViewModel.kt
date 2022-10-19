package com.narui.nkustplatformassistant.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.narui.nkustplatformassistant.data.NkustEvent
import com.narui.nkustplatformassistant.data.persistence.DataRepository


class ScheduleViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _schedules = MutableLiveData(mutableListOf<NkustEvent?>())
    val schedules: LiveData<MutableList<NkustEvent?>> = _schedules



}
