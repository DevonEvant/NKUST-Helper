package com.example.nkustplatformassistant.ui.score


import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.example.nkustplatformassistant.dbDataAvailability
import kotlinx.coroutines.Dispatchers

class ScoreViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _scoreDropdownList = MutableLiveData<List<DropDownParams>>()
    val scoreDropDownList: LiveData<List<DropDownParams>> get() = _scoreDropdownList

    private val _scores = MutableLiveData<List<ScoreEntity>>()
    val scores: LiveData<List<ScoreEntity>> = _scores

    private val _scoreOther = MutableLiveData(
        ScoreOtherEntity(
            "-1", "-1", "-1", "", "",
            null, null, null, null,
        )
    )
    val scoreOther: LiveData<ScoreOtherEntity> get() = _scoreOther

    private suspend fun getScore(year: Int, semester: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _scores.postValue(dataRepository.getSpecScoreDataFromDB(year, semester))
            _scoreOther.postValue(dataRepository.getSpecScoreOtherDataFromDB(year, semester))
        }
    }

    fun onSelectDropDownChange(dropDownParams: DropDownParams) {
        viewModelScope.launch(Dispatchers.IO) {
            getScore(dropDownParams.year, dropDownParams.semester)
        }
    }

    fun rS(l: MutableList<ScoreEntity>) {
        _scores.value = l
    }

    init {
        if (dbDataAvailability.value!!) {
            viewModelScope.launch(Dispatchers.IO) {
                dataRepository.getScoreDropDownList().let {
                    _scoreDropdownList.postValue(it)

                    getScore(it[0].year, it[0].semester)
                }
            }
        }
    }
}