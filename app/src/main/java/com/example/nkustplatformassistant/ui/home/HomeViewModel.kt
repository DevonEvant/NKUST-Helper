package com.example.nkustplatformassistant.ui.home

import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.persistence.DataRepository

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
//    init {
//        val _progress: MutableLiveData<Float> = MutableLiveData(0F)
//        val progress: LiveData<Float> = _progress
//
//        startFetch(_progress)
//    }

    private val _progress: MutableLiveData<Float> = MutableLiveData(0F)
    val progress: LiveData<Float> = _progress

//    private val _currentFetch: MutableLiveData<String> = MutableLiveData()
//    val currentFetch: LiveData<String> = _currentFetch

    // https://stackoverflow.com/questions/71709590/how-to-initialize-a-field-in-viewmodel-with-suspend-method

    // TODO: Save to DB seems broken! for both score and course
    fun startFetch() {
        viewModelScope.launch {
//            _currentFetch.value = "Score"
            _progress.value = 0.33F
            dataRepository.fetchAllScoreToDB()

//            _currentFetch.value = "Course"
            _progress.value = 0.67F
            dataRepository.fetchCourseDataToDB()

            _progress.value = 1F

            // TODO: fetch Schedule to DB
        }
    }
}
