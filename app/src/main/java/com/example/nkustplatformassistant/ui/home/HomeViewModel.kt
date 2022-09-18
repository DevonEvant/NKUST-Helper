package com.example.nkustplatformassistant.ui.home

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nkustplatformassistant.data.CourseTime
import com.example.nkustplatformassistant.data.CurriculumTime
import com.example.nkustplatformassistant.data.ResultOf
import kotlinx.coroutines.launch
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.Calender
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.soywiz.korma.geom.bezier.SegmentEmitter.emit
import kotlinx.coroutines.Dispatchers
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAmount
import java.util.*
import java.util.concurrent.Flow
class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {


    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    val dbHasData: MutableLiveData<Boolean> = MutableLiveData(false)

    fun startFetch(force: Boolean): LiveData<Float> = liveData {
        if (force) {
            dataRepository.fetchAllScoreToDB()
            emit(0.33F)
            dataRepository.fetchCourseDataToDB()
            emit(0.67F)
            // TODO: fetch Schedule to DB, progress didn't change
            emit(1F)
        }
    }

    fun clearDB(force: Boolean) {
        if (force) {
            viewModelScope.launch {
                dataRepository.clearAllDB()
            }
        }
    }

    private val _minuteBefore: MutableLiveData<TemporalAmount> =
        MutableLiveData(Duration.ofMinutes(0L))
    val minuteBefore = _minuteBefore

    enum class SubjectWidgetEnum(name: String) {
        CourseName("課程名稱"), ClassName("開班名稱"), ClassLocation("教室地點"),
        ClassTime("沒用到"), ClassGroup("分組"), Professor("教授"),
        StartTime("上課時間"), EndTime("下課時間"),
    }

    private val _courseWidgetParams: MutableLiveData<Map<SubjectWidgetEnum, String>> =
        MutableLiveData()
    val courseWidgetParams: LiveData<Map<SubjectWidgetEnum, String>> = _courseWidgetParams

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dbHasData.postValue(dataRepository.checkDataIsReady())

            if (dbHasData.value!!)
                _courseWidgetParams.postValue(
                    dataRepository.getCurrentCourse()
                )
        }
    }


//    val courseWidgetParams: LiveData<Map<SubjectWidgetEnum, String>> = liveData {
//        var currentCourse: Map<SubjectWidgetEnum, String> = mapOf()
//        viewModelScope.launch(Dispatchers.IO) {
//            currentCourse = dataRepository.getCurrentCourse(minuteBefore.value!!)
//
//        }
//    }
}

