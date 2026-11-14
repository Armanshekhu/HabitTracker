package com.plcoding.habittracker.data.local

import com.plcoding.habittracker.data.local.entity.HabitCompletionEntity
import com.plcoding.habittracker.data.local.entity.HabitEntity
import com.plcoding.habittracker.domain.model.Habit
import com.plcoding.habittracker.domain.model.HabitCompletion
import com.plcoding.habittracker.domain.model.HabitIcon
import java.time.DayOfWeek
import java.time.LocalDate

fun HabitEntity.toDomain(): Habit {
    val days = if (frequency.isBlank()) {
        emptySet()
    } else {
        frequency.split(",")
            .mapNotNull { name ->
                runCatching { DayOfWeek.valueOf(name.trim()) }.getOrNull()
            }
            .toSet()
    }
    return Habit(
        id = id,
        name = name,
        icon = HabitIcon.fromId(iconId),
        frequency = days,
        createdAt = LocalDate.ofEpochDay(createdAt)
    )
}

fun Habit.toEntity(): HabitEntity {
    val frequencyString = frequency.joinToString(",") { it.name }
    return HabitEntity(
        id = id,
        name = name,
        iconId = icon.id,
        frequency = frequencyString,
        createdAt = createdAt.toEpochDay()
    )
}

fun HabitCompletionEntity.toDomain(): HabitCompletion {
    return HabitCompletion(
        id = id,
        habitId = habitId,
        date = LocalDate.ofEpochDay(date)
    )
}

fun HabitCompletion.toEntity(): HabitCompletionEntity {
    return HabitCompletionEntity(
        id = id,
        habitId = habitId,
        date = date.toEpochDay()
    )
}
