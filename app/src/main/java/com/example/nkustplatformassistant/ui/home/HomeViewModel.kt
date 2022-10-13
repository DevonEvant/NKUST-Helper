package com.example.nkustplatformassistant.ui.home

import androidx.lifecycle.*
import com.example.nkustplatformassistant.data.CurriculumTime
import com.example.nkustplatformassistant.data.DropDownParams
import kotlinx.coroutines.launch
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.example.nkustplatformassistant.dbDataAvailability
import kotlinx.coroutines.Dispatchers
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {


    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    private val _fetchingProgress = MutableLiveData(0F)
    val fetchingProgress: LiveData<Float> get() = _fetchingProgress

    private val _minuteBefore = MutableLiveData(Duration.ofMinutes(15L))
    val minuteBefore: LiveData<Duration> get() = _minuteBefore

    private val _todayCourse: MutableLiveData<List<CourseEntity>> = MutableLiveData()
    val todayCourse: LiveData<List<CourseEntity>> get() = _todayCourse
    private val _recentCourse: MutableLiveData<CourseEntity> = MutableLiveData()
    val recentCourse: LiveData<CourseEntity> get() = _recentCourse

    private val _scoreDropdownList = MutableLiveData<List<DropDownParams>>()
    val scoreDropDownList: LiveData<List<DropDownParams>> get() = _scoreDropdownList

    private val _scoreOther = MutableLiveData(
        ScoreOtherEntity(
            "-1", "-1", "-1", "", "",
            null, null, null, null,
        )
    )
    val scoreOther: LiveData<ScoreOtherEntity> get() = _scoreOther

    fun onScoreOtherDropDownChange(dropDownParams: DropDownParams) {
        viewModelScope.launch {
            getScoreOther(dropDownParams.year, dropDownParams.semester)
        }
    }

    private suspend fun getScoreOther(year: Int, semester: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _scoreOther.postValue(dataRepository.getSpecScoreOtherDataFromDB(year, semester))
        }
    }

    fun getRecentCourse(minuteBefore: Long) {
        val todayWeek = LocalDate.now().dayOfWeek.value
        val currentTime = LocalTime.now()

        _minuteBefore.postValue(Duration.ofMinutes(minuteBefore))

        _todayCourse.value?.forEach { eachCourse ->
            eachCourse.courseTime.forEach { courseTime ->
                if ((courseTime.week!!.ordinal + 1) == todayWeek) {
                    CurriculumTime.getByTime(currentTime)?.time?.let {
                        if (it.include(currentTime + Duration.ofMinutes(minuteBefore))) {
                            _recentCourse.postValue(eachCourse)
                        }
                    }
                }
            }
        }
    }


    fun startFetch(force: Boolean) {
        if (force && DataRepository.loginState) {
            viewModelScope.launch(Dispatchers.IO) {
                dataRepository.fetchAllScoreToDB()
                dataRepository.fetchAllScoreOtherToDB()
                _fetchingProgress.postValue(0.33F)
                dataRepository.fetchCourseDataToDB()
                _fetchingProgress.postValue(0.67F)
                // TODO: fetch Schedule to DB, progress didn't change
                _fetchingProgress.postValue(1F)

                dataRepository.checkDataIsReady().let {
                    dbDataAvailability = it
                }
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
        if (dbDataAvailability) {
            viewModelScope.launch(Dispatchers.IO) {
                _todayCourse.postValue(dataRepository.getTodayCourse())
            }
            viewModelScope.launch(Dispatchers.IO) {
                dataRepository.getScoreDropDownList().let {
                    _scoreDropdownList.postValue(it)

                    getScoreOther(it[0].year, it[0].semester)
                }
            }
        }
    }
}
