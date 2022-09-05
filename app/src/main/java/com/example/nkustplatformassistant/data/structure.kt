package com.example.nkustplatformassistant.data

import java.lang.Exception

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


enum class CurriculumTime(val id: Char, val startTime: String, val endTime: String) {
    _M('M', "07:10", "08:00"),
    _1('1', "08:10", "09:00"),
    _2('2', "09:10", "10:00"),
    _3('3', "10:10", "11:00"),
    _4('4', "11:10", "12:00"),
    _A('A', "12:30", "13:20"),
    _5('5', "13:30", "14:20"),
    _6('6', "14:30", "15:20"),
    _7('7', "15:30", "16:20"),
    _8('8', "16:30", "17:20"),
    _9('9', "17:30", "18:20");

    /**
     * you can use operator "rangeTo" (like 1..3) and return ClosedRange<CurriculumTime>
     */
    operator fun rangeTo(end: CurriculumTime): ClosedRange<CurriculumTime> {
        if (this@CurriculumTime.ordinal > end.ordinal)
            throw Error("start cannot bigger then end")

        return object : ClosedRange<CurriculumTime> {
            override val start: CurriculumTime = this@CurriculumTime
            override val endInclusive: CurriculumTime = end

            /**
             * you can use operator "in" in object "ClosedRange<CurriculumTime>"
             * (like CurriculumTime._A in CurriculumTime._3..CurriculumTime._7 == ture)
             */
            override operator fun contains(value: CurriculumTime): Boolean =
                (endInclusive.ordinal >= value.ordinal && value.ordinal >= start.ordinal)

            override fun isEmpty(): Boolean = start.ordinal > endInclusive.ordinal

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
    }
}

data class CourseTime(
    val week: Weeks?,
    val curriculumTimeRange: ClosedRange<CurriculumTime>
)

sealed class Response<out R> {
    data class Loading(val data: Nothing) : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception) : Response<Exception>()
}

//class TimePicker()


data class Score(
    val subjectName: String,
    val midScore: String,
    val finalScore: String,
)


//data class Course(
//    var id: String,
//    var courseName: String,
//    var className: String,
//    var classGroup: String,
//    var credits: String,
//    var teachingHours: String,
//    var isElectiveSubject: Boolean,
//    var semesterType: String,
//    var session: String,
//    var professor: String,
//    var classLocation: String
//)

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