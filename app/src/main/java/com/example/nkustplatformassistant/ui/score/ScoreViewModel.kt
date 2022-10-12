package com.example.nkustplatformassistant.ui.score


import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.example.nkustplatformassistant.dbDataAvailability
import kotlinx.coroutines.Dispatchers

class ScoreViewModel(private val dataRepository: DataRepository) : ViewModel() {

    init {
        getAllScore()
    }

    private val _scores = MutableLiveData<List<ScoreEntity>>()
    val scores: LiveData<List<ScoreEntity>> = _scores

    private val _scoreOther = MutableLiveData(
        ScoreOtherEntity(
            "-1", "-1", "-1", "0F", "0F",
            null, null, null, null,
        )
    )
    val scoreOther: LiveData<ScoreOtherEntity> get() = _scoreOther

    private fun getAllScore() {
        if (dbDataAvailability) {
            viewModelScope.launch(Dispatchers.IO) {
                val latestScoreParams = dataRepository.getLatestScoreParams()
                _scores.postValue(dataRepository.getSpecScoreDataFromDB(
                    latestScoreParams.year,
                    latestScoreParams.semester,
                ))
            }
        }
    }

    fun rS(l: MutableList<ScoreEntity>) {
        _scores.value = l
    }
}