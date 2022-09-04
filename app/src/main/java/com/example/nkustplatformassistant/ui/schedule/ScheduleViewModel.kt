package com.example.nkustplatformassistant.ui.score

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.*
import androidx.compose.foundation.layout.*
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nkustplatformassistant.data.NkustEvent
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.data.remote.NkustUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import java.io.File


class ScheduleViewModel : ViewModel() {

    private val _schedules = MutableLiveData(mutableListOf<NkustEvent?>())
    val schedules: LiveData<MutableList<NkustEvent?>> = _schedules
//
//    val timeVisibility: LiveData<Boolean> = _timeVisibility
//    val timeCodeVisibility: LiveData<Boolean> = _timeCodeVisibility
//    val courses: LiveData<MutableList<Course>> = _courses
//
//    init {
//        if (_courses.value!!.isEmpty())
//            updataCourses()
//    }
//
//    fun onTimeVisibilityChange() {
//        _timeVisibility.value = !_timeVisibility.value!!
//    }
//
//    fun onTimeCodeVisibilityChange() {
//        _timeCodeVisibility.value = !_timeCodeVisibility.value!!
//    }
//
//    fun updataCourses(refresh: Boolean = false) {
//        throw Error("not complete")
////        _courses.value =
//    }

}
