package com.example.nkustplatformassistant.ui.score


import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import kotlinx.coroutines.Dispatchers

class ScoreViewModel(private val dataRepository: DataRepository) : ViewModel() {

    init {
        getAllScore()
    }

    private val _scores = MutableLiveData(mutableListOf<ScoreEntity>())
    val scores: LiveData<MutableList<ScoreEntity>> = _scores

    fun getAllScore() {

        if (DataRepository.loginState ) {
            viewModelScope.launch(Dispatchers.IO) {
                val latestScoreParams = dataRepository.getLatestScoreParams()
                _scores.postValue(dataRepository.getSpecScoreDataFromDB(
                    latestScoreParams.year,
                    latestScoreParams.semester,
                ) as MutableList<ScoreEntity>)
            }
        }
    }

    fun rS(l: MutableList<ScoreEntity>) {
        _scores.value = l
    }
}