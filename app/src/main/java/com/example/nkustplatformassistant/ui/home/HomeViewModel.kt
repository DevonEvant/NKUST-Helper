package com.example.nkustplatformassistant.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.example.nkustplatformassistant.R
import com.example.nkustplatformassistant.data.CurriculumTime
import com.example.nkustplatformassistant.data.DropDownParams
import kotlinx.coroutines.launch
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.example.nkustplatformassistant.dbDataAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import com.example.nkustplatformassistant.data.persistence.DataRepository.Companion.loginState


class HomeViewModel(private val dataRepository: DataRepository, context: Context) : ViewModel() {


    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    private val _fetchingDetails =
        MutableLiveData(context.getString(R.string.home_viewmodel_waitingresource))
    val fetchingDetails: LiveData<String> get() = _fetchingDetails

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


    fun startFetch(force: Boolean, context: Context) {
        if (force && loginState) {
            viewModelScope.launch(Dispatchers.IO) {
                _fetchingDetails.postValue(context.getString(R.string.home_viewmodel_getscore))
                dataRepository.fetchAllScoreToDB()
                delay(timeMillis = 3000)
                _fetchingDetails.postValue(context.getString(R.string.home_viewmodel_getscoreother))
                dataRepository.fetchAllScoreOtherToDB()
                delay(timeMillis = 3000)
                _fetchingDetails.postValue(context.getString(R.string.home_viewmodel_getcourse))
                dataRepository.fetchCourseDataToDB()
                // TODO: fetch Schedule to DB, progress didn't change
            }.invokeOnCompletion {
                viewModelScope.launch(Dispatchers.IO) {
                    dataRepository.checkDataIsReady().let {
                        dbDataAvailability.postValue(it)
                        loginState = false
                    }
                }
            }
        }
    }

    fun clearDB(force: Boolean) {
        if (force) {
            viewModelScope.launch {
                dbDataAvailability.postValue(false)
                dataRepository.clearAllDB()
            }
        }
    }

    init {
        if (dbDataAvailability.value!!) {
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
