package com.example.nkustplatformassistant.ui.curriculum

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
import com.example.nkustplatformassistant.data.remote.NkustUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import java.io.File

enum class Weeks(val shortCode: String, val cn: String) {
    Mon("M","星期一"),
    Tue("T","星期二"),
    Wed("W","星期三"),
    Thu("T","星期四"),
    Fri("F","星期五"),
    Sat("S","星期六"),
    Sun("S","星期日");

    companion object {
        operator fun get(ordinal: Int): Weeks {
            return values()[ordinal]
        }
    }
}

enum class CurriculumTime(val id: String, val startTime: String, val endTime: String) {
    _M("M", "07:10", "08:00"),
    _1("1", "08:10", "09:00"),
    _2("2", "09:10", "10:00"),
    _3("3", "10:10", "11:00"),
    _4("4", "11:10", "12:00"),
    _A("A", "12:30", "13:20"),
    _5("5", "13:30", "14:20"),
    _6("6", "14:30", "15:20"),
    _7("7", "15:30", "16:20"),
    _8("8", "16:30", "17:20"),
    _9("9", "17:30", "18:20");

    companion object {
        operator fun get(ordinal: Int): CurriculumTime {
            return values()[ordinal]
        }
    }
}


data class Course(
    var id: String,
    var courseName: String,
    var className: String,
    var classGroup: String,
    var credits: String,
    var teachingHours: String,
    var isElectiveSubject: Boolean,
    var semesterType: String,
    var session: String,
    var professor: String,
    var classLocation: String
) {
//    operator fun get(index: Int): String {
//        return when (index) {
//            1 -> id
//            2 -> courseName
//            3 -> className
//            4 -> group
//            5 -> credits
//            6 -> teachingHours
//            7 -> electiveSubject
//            8 -> semesterType
//            9 -> session
//            10 -> instructor
//            11 -> classroom
//            else -> throw Error("The index is not exist.")
//        }
//    }
//
//    operator fun set(index: Int, value: Any) {
//        when (index) {
//            1 -> id = value as String
//            2 -> courseName = value as String
//            3 -> className = value as String
//            4 -> group = value as String
//            5 -> credits = value as String
//            6 -> teachingHours = value as String
//            7 -> electiveSubject= value as String
//            8 -> semesterType = value as String
//            9 -> session = value as String
//            10 -> instructor = value as String
//            11 -> classroom = value as String
//            else -> throw Error("The index is not exist.")
//        }
//    }

}


class CurriculumViewModel : ViewModel() {
    private val _timeVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _timeCodeVisibility: MutableLiveData<Boolean> = MutableLiveData(false)


    val timeVisibility: LiveData<Boolean> = _timeVisibility
    val timeCodeVisibility: LiveData<Boolean> = _timeCodeVisibility

    fun onTimeVisibilityChange() {
        _timeVisibility.value = !_timeVisibility.value!!
    }

    fun onTimeCodeVisibilityChange() {
        _timeCodeVisibility.value = !_timeCodeVisibility.value!!
    }
//
//    fun onPwdChange(newPwd: String) {
//        _pwd.value = newPwd
//    }
//
//    fun onPwdVisibilityReversed() {
//        _pwdVisibility.value = (_pwdVisibility.value)!!.not()
//    }
}





