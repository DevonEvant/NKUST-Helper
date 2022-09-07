package com.example.nkustplatformassistant.ui.home

import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.persistence.DataRepository

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _progress: MutableLiveData<Float> = MutableLiveData(0F)
    val progress: LiveData<Float> = _progress

    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    fun startFetch(force: Boolean) {
        if (force) {
            viewModelScope.launch {
//            _currentFetch.value = "Score"
                _progress.value = 0.33F
                dataRepository.fetchAllScoreToDB()

//            _currentFetch.value = "Course"
                _progress.value = 0.67F
                dataRepository.fetchCourseDataToDB()


                _progress.value = 1F

                // TODO: fetch Schedule to DB, progress didn't change
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

    fun checkDBHasData(): Boolean {
        var exist = false
        viewModelScope.launch {
            exist = dataRepository.checkDataIsReady()
        }
        return exist
    }
}
