package com.example.nkustplatformassistant.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.nkustplatformassistant.data.persistence.DataRepository
import kotlinx.coroutines.Dispatchers
import java.time.Duration

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {


    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    val dbHasData: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _fetchingProgress = MutableLiveData(0F)
    val fetchingProgress: LiveData<Float> get() = _fetchingProgress

    private val _minuteBefore = MutableLiveData(Duration.ofMinutes(15L))
    val minuteBefore: LiveData<Duration> get() = _minuteBefore

    enum class SubjectWidgetEnum(name: String) {
        CourseName("課程名稱"), ClassName("開班名稱"), ClassLocation("教室地點"),
        ClassTime("沒用到"), ClassGroup("分組"), Professor("教授"),
        StartTime("上課時間"), EndTime("下課時間"),
    }

    fun getRecentCourse(minute: Long) {
        _minuteBefore.postValue(Duration.ofMinutes(minute))
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: course update
            dataRepository.getCurrentCourse(Duration.ofMinutes(minute))
        }
    }


    private val _courseWidgetParams: MutableLiveData<Map<SubjectWidgetEnum, String>> =
        MutableLiveData()
    val courseWidgetParams: LiveData<Map<SubjectWidgetEnum, String>> = _courseWidgetParams

    fun startFetch(force: Boolean) {
        if (force && DataRepository.loginState) {
            viewModelScope.launch(Dispatchers.IO) {
                dataRepository.fetchAllScoreToDB()
                _fetchingProgress.postValue(0.33F)
                dataRepository.fetchCourseDataToDB()
                _fetchingProgress.postValue(0.67F)
                // TODO: fetch Schedule to DB, progress didn't change
                _fetchingProgress.postValue(1F)
            }
        }
    }

    fun clearDB(force: Boolean) {
        if (force) {
            viewModelScope.launch {
                dataRepository.clearAllDB()
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dbHasData.postValue(dataRepository.checkDataIsReady())

            if (dbHasData.value!!) {
                _courseWidgetParams.postValue(
                    dataRepository.getCurrentCourse(Duration.ofMinutes(15L))
                )
            }
        }
    }
}

