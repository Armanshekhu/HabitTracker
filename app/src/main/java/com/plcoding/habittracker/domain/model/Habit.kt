package com.plcoding.habittracker.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

data class Habit(
    val id: Long = 0L,
    val name: String,
    val icon: HabitIcon,
    val frequency: Set<DayOfWeek>,
    val createdAt: LocalDate
)
