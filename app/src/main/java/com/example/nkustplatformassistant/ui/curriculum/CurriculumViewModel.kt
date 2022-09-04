package com.example.nkustplatformassistant.ui.curriculum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity


class CurriculumViewModel : ViewModel() {
    private val _timeVisibility = MutableLiveData(false)
    private val _timeCodeVisibility = MutableLiveData(false)
    private val _courses = MutableLiveData(mutableListOf<CourseEntity>())


    val timeVisibility: LiveData<Boolean> = _timeVisibility
    val timeCodeVisibility: LiveData<Boolean> = _timeCodeVisibility
    val courses: LiveData<MutableList<CourseEntity>> = _courses

    init {
        if (_courses.value!!.isEmpty())
            updataCourses()
    }

    fun onTimeVisibilityChange() {
        _timeVisibility.value = !_timeVisibility.value!!
    }

    fun onTimeCodeVisibilityChange() {
        _timeCodeVisibility.value = !_timeCodeVisibility.value!!
    }

    fun updataCourses(refresh: Boolean = false) {
        throw Error("not complete")
//        _courses.value =
    }

}





