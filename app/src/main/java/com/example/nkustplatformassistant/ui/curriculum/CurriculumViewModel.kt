package com.example.nkustplatformassistant.ui.curriculum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CurriculumViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _startTimeVisibility = MutableLiveData(true)
    private val _endTimeVisibility = MutableLiveData(true)
    private val _courses = MutableLiveData(listOf<CourseEntity>())


    val startTimeVisibility: LiveData<Boolean> get() = _startTimeVisibility
    val endTimeVisibility: LiveData<Boolean> get() = _endTimeVisibility
    val courses: LiveData<List<CourseEntity>> get() = _courses

    fun onStartTimeVisibilityChange() {
        _startTimeVisibility.value = !_startTimeVisibility.value!!
    }

    fun onEndTimeVisibilityChange() {
        _endTimeVisibility.value = !_endTimeVisibility.value!!
    }

    fun updataCourses(refresh: Boolean = false) {
//        _courses.value =
    }


    private val _dropDownParams = MutableLiveData(listOf<DropDownParams>())
    val dropDownParams: LiveData<List<DropDownParams>> = _dropDownParams

    // TODO: check before display and show circular progress bar
    init {
        viewModelScope.launch(Dispatchers.IO) {
            _dropDownParams.postValue(
                dataRepository.getDropDownListFromDB()
            )
        }
    }

    suspend fun getCourse(year: Int, semester: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _courses.postValue(dataRepository.getSpecCurriculumCourse(year, semester))
        }
    }

//    fun t() {
//        val a = CourseEntity(
//            1,
//            1,
//            1,
//            "1",
//            "1",
//            "1",
//            "1",
//            "1",
//            "1",
//            "1",
//            "1",
//            "1",
//            false
//        ).let {
//            it.courseTime = listOf(CourseTime(
//                week = Weeks.Wed,
//                curriculumTimeRange = CurriculumTime._2..CurriculumTime._8
//            ))
//            it
//        }
//        _courses.postValue(listOf(a))
//    }

}





