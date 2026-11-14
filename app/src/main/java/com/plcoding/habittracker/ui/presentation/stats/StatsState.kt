package com.plcoding.habittracker.ui.presentation.stats

import com.plcoding.habittracker.domain.model.HabitWithStreak
import java.time.LocalDate

data class HeatmapDay(
    val date: LocalDate,
    val completionRatio: Float, // 0f to 1f
    val isToday: Boolean,
    val isFuture: Boolean
)

data class StatsState(
    val thisWeekPercentage: Int = 0,
    val bestStreak: Int = 0,
    val activeHabitsCount: Int = 0,
    val heatmapWeeks: List<List<HeatmapDay>> = emptyList(), // List of 4 weeks, each with 7 days (Mon-Sun)
    val habitStreaks: List<HabitWithStreak> = emptyList(),
    val isLoading: Boolean = true
)
