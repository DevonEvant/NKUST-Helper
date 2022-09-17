package com.example.nkustplatformassistant.ui.home

import androidx.lifecycle.*
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Flow

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {


    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    val dbHasData: LiveData<Boolean> = liveData {
        emit(dataRepository.checkDataIsReady())
    }

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

    enum class SubjectWidgetEnum {
        CourseName, ClassName, ClassLocation, ClassTime, ClassGroup, Professor, StartTime, EndTime,
    }

    // ResultOf.Error 表示今天沒課
    val courseWidgetParams: LiveData<Map<SubjectWidgetEnum, String>> = liveData {
        viewModelScope.launch(Dispatchers.IO) {
            val currentCourse = dataRepository.getCurrentCourse()
            emit(currentCourse)
        }
    }
}
