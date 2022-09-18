package com.example.nkustplatformassistant.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.util.rangeTo
import java.time.LocalTime

// https://kotlinlang.org/docs/generics.html#type-projections
// https://stackoverflow.com/questions/48051190/kotlin-one-type-argument-expected-for-class-for-abstract-generic-view-holder
sealed class ResultOf<out T> {
    data class Success<out R>(val value: R) : ResultOf<R>()
    data class Error(val message: String?) : ResultOf<Nothing>()
}

//sealed class Response<out R> {
//    data class Loading(val data: Nothing) : Response<Nothing>()
//    data class Success<out T>(val data: T) : Response<T>()
//    data class Error(val exception: Exception) : Response<Exception>()
//}

enum class Weeks(val shortCode: Char, val cn: String, val shortCnCode: Char) {
    Mon('M', "星期一", '一'),
    Tue('T', "星期二", '二'),
    Wed('W', "星期三", '三'),
    Thu('T', "星期四", '四'),
    Fri('F', "星期五", '五'),
    Sat('S', "星期六", '六'),
    Sun('S', "星期日", '日');

    companion object {
        /**
         * you can use operator '[ ]' to get the corresponding item from ordinal
         */
        operator fun get(ordinal: Int): Weeks {
            return values()[ordinal]
        }

        /**
         * get the corresponding item by weekShortCnCode(like '一','四','五','日')
         */
        fun getByShortCnCode(shortCnCode: Char): Weeks? {
            for (v in values()) {
                if (v.shortCnCode == shortCnCode)
                    return v
            }
            return null
        }
    }
}

enum class CurriculumTime(
    val id: Char,
//    val startTime: String,
//    val endTime: String,
    val time: HourAndMinute.HourAndMinuteRange,
) {
//    _M('M', "07:10", "08:00", HourAndMinute(7, 10)..HourAndMinute(8, 0)),
//    _1('1', "08:10", "09:00", HourAndMinute(8, 10)..HourAndMinute(9, 0)),
//    _2('2', "09:10", "10:00", HourAndMinute(9, 10)..HourAndMinute(10, 0)),
//    _3('3', "10:10", "11:00", HourAndMinute(10, 10)..HourAndMinute(11, 0)),
//    _4('4', "11:10", "12:00", HourAndMinute(11, 10)..HourAndMinute(12, 0)),
//    _A('A', "12:30", "13:20", HourAndMinute(12, 30)..HourAndMinute(13, 20)),
//    _5('5', "13:30", "14:20", HourAndMinute(13, 30)..HourAndMinute(14, 20)),
//    _6('6', "14:30", "15:20", HourAndMinute(14, 30)..HourAndMinute(15, 20)),
//    _7('7', "15:30", "16:20", HourAndMinute(15, 30)..HourAndMinute(16, 20)),
//    _8('8', "16:30", "17:20", HourAndMinute(16, 30)..HourAndMinute(17, 20)),
//    _9('9', "17:30", "18:20", HourAndMinute(17, 30)..HourAndMinute(18, 20));

    _M('M', HourAndMinute(7, 10)..HourAndMinute(8, 0)),
    _1('1', HourAndMinute(8, 10)..HourAndMinute(9, 0)),
    _2('2', HourAndMinute(9, 10)..HourAndMinute(10, 0)),
    _3('3', HourAndMinute(10, 10)..HourAndMinute(11, 0)),
    _4('4', HourAndMinute(11, 10)..HourAndMinute(12, 0)),
    _A('A', HourAndMinute(12, 30)..HourAndMinute(13, 20)),
    _5('5', HourAndMinute(13, 30)..HourAndMinute(14, 20)),
    _6('6', HourAndMinute(14, 30)..HourAndMinute(15, 20)),
    _7('7', HourAndMinute(15, 30)..HourAndMinute(16, 20)),
    _8('8', HourAndMinute(16, 30)..HourAndMinute(17, 20)),
    _9('9', HourAndMinute(17, 30)..HourAndMinute(18, 20));

    class HourAndMinute(hour: Int, minute: Int) : Comparable<HourAndMinute> {

        // todo HourAndMinute("11:12") -> HourAndMinute(11,12)
//        constructor(timeDescription: String) : this() {
//            val (hr, min) = timeDescription.split(":", limit = 1).map { it.toInt() }
//            HourAndMinute(hr, min)
//        }

        var hour: Int = hour
            get() = if (field in 0 until 24) field else throw Error("illegal value")

        var minute: Int = minute
            get() = if (field in 0 until 60) field else throw Error("illegal value")

        override operator fun compareTo(other: HourAndMinute): Int {
            if (hour == other.hour)
                return minute - other.minute
            return hour - other.hour
        }

        /**
         * declare type when you use operator rangeTo
         */
        abstract class HourAndMinuteRange : ClosedRange<HourAndMinute> {
            abstract infix fun include(value: LocalTime): Boolean
        }

        operator fun rangeTo(other: HourAndMinute): HourAndMinuteRange {
            return object : HourAndMinuteRange() {
                override val start = this@HourAndMinute
                override val endInclusive = other

                /**
                 * check if HourAndMinuteRange include [value] (the [value]'s type is [LocalTime])
                 */
                @RequiresApi(Build.VERSION_CODES.O)
                override infix fun include(value: LocalTime): Boolean {
                    if (value in LocalTime.of(this.start.hour, this.start.minute)
                        rangeTo LocalTime.of(this.endInclusive.hour, this.endInclusive.minute)
                    ) {
                        return true
                    }
                    return false
//                    if (value.hour in this.start.hour..this.endInclusive.hour) {
//                        if (value.hour == this.start.hour) {
//                            if (value.minute < this.start.minute)
//                                return false
//                            else if (value.minute > this.start.minute)
//                                return true
//                            return value.second >= 0
//                        } else if (value.hour == this.endInclusive.hour) {
//                            if (value.minute < this.endInclusive.minute)
//                                return true
//                            else if (value.minute > this.endInclusive.minute)
//                                return false
//                            return value.second <= 0
//                        }
//                        return true
//                    }
//                    return false
                }
            }
        }

        /**
         * covert time of the object to Iso standard (HH:MM)
         * if time less then two digits when [fill] is ture, I will fill string with 0
         */
        fun toIsoDescription(fill: Boolean = true): String {
            if (fill)
                return String.format("%02d:%02d", hour, minute)
            return String.format("%d:%d", hour, minute)

        }
    }

    /**
     * declare type when you use operator rangeTo
     */
    abstract class CurriculumTimeRange : ClosedRange<CurriculumTime> {
        abstract infix fun include(value: LocalTime): Boolean
    }

    /**
     * you can use operator "rangeTo" (like 1..3) and return ClosedRange<CurriculumTime>
     */
    operator fun rangeTo(end: CurriculumTime): CurriculumTimeRange {
        if (this@CurriculumTime.ordinal > end.ordinal)
            throw Error("start cannot bigger then end")

        return object : CurriculumTimeRange() {
            override val start: CurriculumTime = this@CurriculumTime
            override val endInclusive: CurriculumTime = end

            /**
             * you can use operator "in" in object "ClosedRange<CurriculumTime>"
             * (like CurriculumTime._A in CurriculumTime._3..CurriculumTime._7 == ture)
             */
            override operator fun contains(value: CurriculumTime): Boolean =
                (endInclusive.ordinal >= value.ordinal && value.ordinal >= start.ordinal)

            override fun isEmpty(): Boolean = start.ordinal > endInclusive.ordinal

            @RequiresApi(Build.VERSION_CODES.O)
            override infix fun include(value: LocalTime): Boolean {
                if (value in LocalTime.of(
                        this.start.time.start.hour,
                        this.start.time.start.minute
                    )
                    rangeTo LocalTime.of(
                        this.endInclusive.time.endInclusive.hour,
                        this.endInclusive.time.endInclusive.minute
                    )
                ) {
                    return true
                }
                return false
            }
        }
    }

    companion object {
        /**
         * you can use operator '[ ]' to get the corresponding item from ordinal
         */
        operator fun get(ordinal: Int): CurriculumTime {
            return values()[ordinal]
        }

        /**
         * get the corresponding item by CurriculumTimeId(like 'M','1','A','9')
         */
        fun getById(id: Char): CurriculumTime? {
            for (v in values()) {
                if (v.id == id.uppercaseChar())
                    return v
            }
            return null
        }

        fun getByTime(time: LocalTime): CurriculumTime? {
            for (v in values()) {
                if (v.time include time)
                    return v
            }
            return null
        }
    }
}

data class CourseTime(
    val week: Weeks?,
    val curriculumTimeRange: ClosedRange<CurriculumTime>,
)

//class TimePicker()


data class Score(
    val subjectName: String,
    val midScore: String,
    val finalScore: String,
)

data class DropDownParams(
    val year: Int,
    val semester: Int,
    val semDescription: String,
)

interface OpenedRange<T> {

    companion object {
        /**
         * INFINITY single
         */
        val noBound = null
    }

    public val start: T?

    public val endInclusive: T?

    public operator fun contains(value: T): Boolean

}

//enum class Day {
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

//}

class NkustEvent(
    val agency: String,
    val time: OpenedRange<MonthAndDay>,
    val description: String,
) {


    class MonthAndDay(month: Int?, day: Int?) {
        companion object {
            var noBound = MonthAndDay(null, null)
        }

        var month: Int? = month
            get() = if (field == null || field in 0..11) field else throw Error("illegal value")

        var day: Int? = day
            get() = if (field == null || field in 1..31) field else throw Error("illegal value")

        operator fun compareTo(other: MonthAndDay): Int {
            if (month == null || day == null) throw Error("must be given a definite time")
            if (other.month == null || other.day == null) throw Error("must be given a definite time")

            if (month == other.month)
                return day!! - other.day!!
            return month!! - other.month!!
        }

        operator fun rangeTo(other: MonthAndDay): OpenedRange<MonthAndDay> {
            return object : OpenedRange<MonthAndDay> {
                override val start =
                    if (month == null || day == null) null else this@MonthAndDay

                override val endInclusive =
                    if (other.month == null || other.day == null) null else other

                override operator fun contains(value: MonthAndDay): Boolean {
                    if (value.month == null || value.day == null)
                        throw  Error("must be given a definite time")

                    return when {
                        (start == null) && (endInclusive == null) -> true
                        (start == null) -> (value <= endInclusive!!)
                        (endInclusive == null) -> (value >= start)

                        else -> value >= start && value <= endInclusive
                    }
                }
            }
        }
    }


}

enum class Agency {
    OfficeOfTheSecretariat,
    PersonnelOffice,
    CurriculumDivision,
    RegistrationDivision,
    StudentLearningCounselingDivision,
    OfficeOfStudent,
    OfficeOfGeneralAffairs,
    OfficeOfPhysicalEducation,
    CenterOfGeneralStudies,
    ForeignLanguageEducationCente
}