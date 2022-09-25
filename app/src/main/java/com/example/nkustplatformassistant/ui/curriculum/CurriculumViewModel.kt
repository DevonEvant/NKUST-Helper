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
    private val _dropDownParams = MutableLiveData(listOf<DropDownParams>())
    private val _currSelectDropDown = MutableLiveData(
        DropDownParams(-1, -1, ""))

    val startTimeVisibility: LiveData<Boolean> get() = _startTimeVisibility
    val endTimeVisibility: LiveData<Boolean> get() = _endTimeVisibility
    val courses: LiveData<List<CourseEntity>> get() = _courses
    val dropDownParams: LiveData<List<DropDownParams>> = _dropDownParams
    val currSelectDropDown: LiveData<DropDownParams> get() = _currSelectDropDown

    fun onStartTimeVisibilityChange() {
        _startTimeVisibility.value = !_startTimeVisibility.value!!
    }

    fun onEndTimeVisibilityChange() {
        _endTimeVisibility.value = !_endTimeVisibility.value!!
    }

    fun updataCourses(refresh: Boolean = false) {
//        _courses.value =
    }

    // TODO: check before display and show circular progress bar
    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.getDropDownListFromDB().let {
                _dropDownParams.postValue(it)
                _currSelectDropDown.postValue(it[0])
            }
        }
    }

    suspend fun getCourse(year: Int, semester: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _courses.postValue(dataRepository.getSpecCurriculumCourse(year, semester))
        }
    }

    fun onSelectDropDownChange(selectedDropDown: DropDownParams) {
        viewModelScope.launch(Dispatchers.IO) {
            getCourse(selectedDropDown.year, selectedDropDown.semester)
        }
    }


}





