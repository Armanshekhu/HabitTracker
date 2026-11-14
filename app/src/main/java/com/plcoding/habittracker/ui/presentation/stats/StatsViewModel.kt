package com.plcoding.habittracker.ui.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.habittracker.domain.model.HabitWithStreak
import com.plcoding.habittracker.domain.repository.HabitRepository
import com.plcoding.habittracker.domain.util.StreakCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatsState())
    val state: StateFlow<StatsState> = _state.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        val today = LocalDate.now()
        combine(
            repository.getAllHabits(),
            repository.getAllCompletions()
        ) { habits, completions ->
            val completionsByHabit = completions.groupBy { it.habitId }
            val completionsByDate = completions.groupBy { it.date }

            // 1. Calculate Habit With Streaks
            val habitStreaks = habits.map { habit ->
                val habitCompletions = completionsByHabit[habit.id].orEmpty()
                val completedDates = habitCompletions.map { it.date }.toSet()
                val isCompletedToday = completedDates.contains(today)
                val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, today)
                val bestStreak = StreakCalculator.calculateBestStreak(habit, completedDates, today)

                HabitWithStreak(
                    habit = habit,
                    isCompletedToday = isCompletedToday,
                    currentStreak = currentStreak,
                    bestStreak = bestStreak
                )
            }

            // 2. Active habits count
            val activeCount = habits.size

            // 3. Best streak across all habits
            val maxBestStreak = habitStreaks.maxOfOrNull { it.bestStreak } ?: 0

            // 4. This Week percentage (Monday up to today)
            val currentMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            var scheduledSlotsThisWeek = 0
            var completedSlotsThisWeek = 0
            var checkDay = currentMonday
            while (!checkDay.isAfter(today)) {
                val scheduledForDay = habits.filter {
                    it.frequency.contains(checkDay.dayOfWeek) && !checkDay.isBefore(it.createdAt)
                }
                scheduledSlotsThisWeek += scheduledForDay.size

                val completedToday = completionsByDate[checkDay].orEmpty()
                val completedScheduled = completedToday.count { comp ->
                    scheduledForDay.any { it.id == comp.habitId }
                }
                completedSlotsThisWeek += completedScheduled

                checkDay = checkDay.plusDays(1)
            }

            val thisWeekPercentage = if (scheduledSlotsThisWeek > 0) {
                ((completedSlotsThisWeek.toFloat() / scheduledSlotsThisWeek) * 100).toInt()
            } else {
                0
            }

            // 5. Activity Heatmap (4 weeks x 7 days)
            // W1 = 3 weeks ago, W2 = 2 weeks ago, W3 = 1 week ago, W4 = current week
            val startMonday = currentMonday.minusWeeks(3)
            val heatmapWeeks = (0..3).map { weekIndex ->
                val weekMonday = startMonday.plusWeeks(weekIndex.toLong())
                (0..6).map { dayOffset ->
                    val dayDate = weekMonday.plusDays(dayOffset.toLong())
                    val isToday = dayDate.isEqual(today)
                    val isFuture = dayDate.isAfter(today)

                    val scheduledForDay = habits.filter {
                        it.frequency.contains(dayDate.dayOfWeek) && !dayDate.isBefore(it.createdAt)
                    }
                    val completedToday = completionsByDate[dayDate].orEmpty()
                    val completedScheduled = completedToday.count { comp ->
                        scheduledForDay.any { it.id == comp.habitId }
                    }

                    val ratio = if (scheduledForDay.isNotEmpty()) {
                        completedScheduled.toFloat() / scheduledForDay.size
                    } else {
                        0f
                    }

                    HeatmapDay(
                        date = dayDate,
                        completionRatio = ratio.coerceIn(0f, 1f),
                        isToday = isToday,
                        isFuture = isFuture
                    )
                }
            }

            _state.update {
                it.copy(
                    thisWeekPercentage = thisWeekPercentage,
                    bestStreak = maxBestStreak,
                    activeHabitsCount = activeCount,
                    heatmapWeeks = heatmapWeeks,
                    habitStreaks = habitStreaks,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}
