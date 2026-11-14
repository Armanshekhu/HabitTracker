package com.plcoding.habittracker.ui.presentation.today

import com.plcoding.habittracker.domain.model.HabitWithStreak
import java.time.LocalDate

data class TodayState(
    val date: LocalDate = LocalDate.now(),
    val habits: List<HabitWithStreak> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true
)
