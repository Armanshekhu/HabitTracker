package com.plcoding.habittracker.domain.repository

import com.plcoding.habittracker.domain.model.Habit
import com.plcoding.habittracker.domain.model.HabitCompletion
import com.plcoding.habittracker.domain.model.HabitWithStreak
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    fun getHabitById(id: Long): Flow<Habit?>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: Long)
    
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletion>>
    fun getAllCompletions(): Flow<List<HabitCompletion>>
    fun getCompletionsForDate(date: LocalDate): Flow<List<HabitCompletion>>
    suspend fun toggleCompletion(habitId: Long, date: LocalDate)
    
    fun getHabitsWithStreaks(date: LocalDate = LocalDate.now()): Flow<List<HabitWithStreak>>
}
