package com.plcoding.habittracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plcoding.habittracker.data.local.entity.HabitCompletionEntity
import com.plcoding.habittracker.data.local.entity.HabitEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitCompletionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}
