package com.plcoding.habittracker.di

import android.app.Application
import androidx.room.Room
import com.plcoding.habittracker.data.local.HabitCompletionDao
import com.plcoding.habittracker.data.local.HabitDao
import com.plcoding.habittracker.data.local.HabitDatabase
import com.plcoding.habittracker.data.repository.HabitRepositoryImpl
import com.plcoding.habittracker.domain.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(app: Application): HabitDatabase {
        return Room.databaseBuilder(
            app,
            HabitDatabase::class.java,
            "habit_tracker.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(db: HabitDatabase): HabitDao {
        return db.habitDao()
    }

    @Provides
    @Singleton
    fun provideHabitCompletionDao(db: HabitDatabase): HabitCompletionDao {
        return db.habitCompletionDao()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(
        habitDao: HabitDao,
        habitCompletionDao: HabitCompletionDao
    ): HabitRepository {
        return HabitRepositoryImpl(habitDao, habitCompletionDao)
    }
}
