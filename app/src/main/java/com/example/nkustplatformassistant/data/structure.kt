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
     * you can use operator '[ ]' to get the corresponding item from ordinal
     */
    operator fun get(ordinal: Int): CurriculumTime {
        return values()[ordinal]
    }

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

    /**
     * get the corresponding item by CurriculumTimeId(like 'M','1','A','9')
     */
    fun getById(id: Char): CurriculumTime? {
        for (v in values()) {
            if (v.id == id)
                return v
        }
        return null
    }
}

sealed class Response<out R> {
    data class Loading(val data: Nothing) : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception) : Response<Exception>()
}

data class Score(
    val subjectName: String,
    val midScore: String,
    val finalScore: String,
)


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

data class NkustEvent(
    val agency: String,
    val time: String,
    val description: String,
)

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