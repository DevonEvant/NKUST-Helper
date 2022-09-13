package com.example.nkustplatformassistant.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.soywiz.korma.geom.bezier.SegmentEmitter.emit

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {


    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    fun startFetch(force: Boolean): LiveData<Float> = liveData {
        dataRepository.fetchAllScoreToDB()
        emit(0.33F)
        dataRepository.fetchCourseDataToDB()
        emit(0.67F)
        // TODO: fetch Schedule to DB, progress didn't change
        emit(1F)
    }

    fun clearDB(force: Boolean) {
        if (force) {
            viewModelScope.launch {
                dataRepository.clearAllDB()
            }
        }
    }

    val dbHasData: LiveData<Boolean> = liveData {
        emit(dataRepository.checkDataIsReady())
    }

    enum class SubjectWidgetEnum {
        CourseName, ClassName, ClassLocation, ClassTime, ClassGroup, Professor, StartTime, EndTime,
    }

    val courseWidgetParams: LiveData<Map<SubjectWidgetEnum, String>> = liveData {
        val courseEntity = dataRepository.getCurrentCourse()
        emit(
            mapOf(
                SubjectWidgetEnum.CourseName to courseEntity.courseName,
                SubjectWidgetEnum.ClassName to courseEntity.className,
                SubjectWidgetEnum.ClassLocation to courseEntity.classLocation,
                SubjectWidgetEnum.ClassTime to courseEntity.classTime,
                SubjectWidgetEnum.ClassGroup to courseEntity.classGroup,
                SubjectWidgetEnum.Professor to courseEntity.professor,
                SubjectWidgetEnum.StartTime to courseEntity.courseTime[0].curriculumTimeRange.start.startTime,
                SubjectWidgetEnum.EndTime to courseEntity.courseTime[1].curriculumTimeRange.endInclusive.endTime
            )
        )
    }
}
