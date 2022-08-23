package com.example.nkustplatformassistant.data.persistence

import com.example.nkustplatformassistant.data.persistence.db.dao.CalenderDao
import com.example.nkustplatformassistant.data.persistence.db.dao.ScoreDao

class DataRepository(
    private val scoreDao: ScoreDao,
    private val calenderDao: CalenderDao
) {

}