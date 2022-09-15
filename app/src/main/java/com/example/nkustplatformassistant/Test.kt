package com.example.nkustplatformassistant

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nkustplatformassistant.data.CurriculumTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
fun main(){
    val t = CurriculumTime.getByTime(LocalTime.parse("14:30:01"))!!
    println(t) // eq CurriculumTime._6
    println(t.time.start.toIsoDescription()) //14:30
    println(t.time.endInclusive.toIsoDescription()) //15:20

    println(t.time include LocalTime.parse("13:59:00"))//False
    println(t.time include LocalTime.parse("14:00:00"))//False
    println(t.time include LocalTime.parse("14:29:00"))//False
    println(t.time include LocalTime.parse("14:30:01"))//True
    println(t.time include LocalTime.parse("14:31:01"))//True
    println(t.time include LocalTime.parse("15:00:00"))//True
    println(t.time include LocalTime.parse("15:19:00"))//True
    println(t.time include LocalTime.parse("15:20:01"))//False
    println(t.time include LocalTime.parse("15:21:00"))//False
    println(t.time include LocalTime.parse("15:30:00"))//False
    println(t.time include LocalTime.parse("15:31:00"))//False
}