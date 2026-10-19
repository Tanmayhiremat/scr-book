package com.scrcasebook.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PatientEntity::class,
        FollowUpEntity::class,
        SectionPageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ScrDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun sectionPageDao(): SectionPageDao

    companion object {
        @Volatile
        private var INSTANCE: ScrDatabase? = null

        fun getDatabase(context: Context): ScrDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScrDatabase::class.java,
                    "scr_casebook.db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
