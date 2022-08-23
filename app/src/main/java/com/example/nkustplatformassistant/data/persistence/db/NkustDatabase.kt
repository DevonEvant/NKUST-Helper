package com.example.nkustplatformassistant.data.persistence.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nkustplatformassistant.data.persistence.db.dao.CalenderDao
import com.example.nkustplatformassistant.data.persistence.db.dao.ScoreDao
import com.example.nkustplatformassistant.data.persistence.db.entity.Calender
import com.example.nkustplatformassistant.data.persistence.db.entity.Score

/* Reference:
 * https://wm4n.github.io/Android-Room%20Database-%E7%AB%8B%E9%A6%AC%E4%B8%8A%E6%89%8B%E5%B0%B1%E7%9C%8B%E9%80%99%E7%AF%87/
 * https://developer.android.com/codelabs/android-room-with-a-view-kotlin?hl=zh-tw#7
 */
@Database(entities = arrayOf(Calender::class, Score::class), version = 1, exportSchema = false)
abstract class NkustDatabase : RoomDatabase() {

    abstract fun calenderDao(): CalenderDao
    abstract fun ScoreDao(): ScoreDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        var INSTANCE: NkustDatabase? = null


        /*
         * getDatabase returns the singleton.
         * It'll create the database the first time it's accessed,
         * using Room's database builder to create a RoomDatabase object in
         * the application context from the WordRoomDatabase class
         * and names it "word_database".
         */
        fun getDatabase(context: Context): NkustDatabase {
            /* if the INSTANCE is not null, then return it,
             * if it is, then create the database
             */
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NkustDatabase::class.java,
                "nkust_database"
            ).build()
            INSTANCE = instance
            return instance
        }
    }

}