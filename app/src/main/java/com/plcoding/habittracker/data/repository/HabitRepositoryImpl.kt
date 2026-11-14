package com.plcoding.habittracker.data.repository

import com.plcoding.habittracker.data.local.HabitCompletionDao
import com.plcoding.habittracker.data.local.HabitDao
import com.plcoding.habittracker.data.local.entity.HabitCompletionEntity
import com.plcoding.habittracker.data.local.toDomain
import com.plcoding.habittracker.data.local.toEntity
import com.plcoding.habittracker.domain.model.Habit
import com.plcoding.habittracker.domain.model.HabitCompletion
import com.plcoding.habittracker.domain.model.HabitWithStreak
import com.plcoding.habittracker.domain.repository.HabitRepository
import com.plcoding.habittracker.domain.util.StreakCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class HabitRepositoryImpl(
    private val habitDao: HabitDao,
    private val habitCompletionDao: HabitCompletionDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getHabitById(id: Long): Flow<Habit?> {
        return habitDao.getHabitById(id).map { it?.toDomain() }
    }

    override fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletion>> {
        return habitCompletionDao.getCompletionsForHabit(habitId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllCompletions(): Flow<List<HabitCompletion>> {
        return habitCompletionDao.getAllCompletions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCompletionsForDate(date: LocalDate): Flow<List<HabitCompletion>> {
        return habitCompletionDao.getCompletionsForDate(date.toEpochDay()).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertHabit(habit: Habit): Long {
        return habitDao.insertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habitId: Long) {
        habitDao.deleteHabit(habitId)
    }

    override suspend fun toggleCompletion(habitId: Long, date: LocalDate) {
        val epochDay = date.toEpochDay()
        val existing = habitCompletionDao.getCompletion(habitId, epochDay)
        if (existing != null) {
            habitCompletionDao.deleteCompletion(habitId, epochDay)
        } else {
            habitCompletionDao.insertCompletion(
                HabitCompletionEntity(
                    habitId = habitId,
                    date = epochDay
                )
            )
        }
    }

    override fun getHabitsWithStreaks(date: LocalDate): Flow<List<HabitWithStreak>> {
        return combine(
            habitDao.getAllHabits(),
            habitCompletionDao.getAllCompletions()
        ) { habitEntities, completionEntities ->
            val completions = completionEntities.map { it.toDomain() }
            val completionsByHabit = completions.groupBy { it.habitId }

            habitEntities.map { entity ->
                val habit = entity.toDomain()
                val habitCompletions = completionsByHabit[habit.id].orEmpty()
                val completedDates = habitCompletions.map { it.date }.toSet()

                val isCompletedToday = completedDates.contains(date)
                val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, date)
                val bestStreak = StreakCalculator.calculateBestStreak(habit, completedDates, date)

                HabitWithStreak(
                    habit = habit,
                    isCompletedToday = isCompletedToday,
                    currentStreak = currentStreak,
                    bestStreak = bestStreak
                )
            }
        }
    }
}
