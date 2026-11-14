package com.plcoding.habittracker.domain.model

data class HabitWithStreak(
    val habit: Habit,
    val isCompletedToday: Boolean,
    val currentStreak: Int,
    val bestStreak: Int
)
