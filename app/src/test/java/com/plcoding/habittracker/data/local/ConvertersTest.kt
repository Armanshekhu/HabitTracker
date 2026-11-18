package com.plcoding.habittracker.data.local

import com.plcoding.habittracker.data.local.entity.HabitCompletionEntity
import com.plcoding.habittracker.data.local.entity.HabitEntity
import com.plcoding.habittracker.domain.model.Habit
import com.plcoding.habittracker.domain.model.HabitCompletion
import com.plcoding.habittracker.domain.model.HabitIcon
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class ConvertersTest {

    @Test
    fun `HabitEntity toDomain maps all fields correctly`() {
        val entity = HabitEntity(
            id = 42L,
            name = "Morning Meditation",
            iconId = "meditate",
            frequency = "MONDAY,WEDNESDAY,FRIDAY",
            createdAt = LocalDate.of(2026, 3, 1).toEpochDay()
        )

        val domain = entity.toDomain()

        assertEquals(42L, domain.id)
        assertEquals("Morning Meditation", domain.name)
        assertEquals(HabitIcon.MEDITATE, domain.icon)
        assertEquals(
            setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            domain.frequency
        )
        assertEquals(LocalDate.of(2026, 3, 1), domain.createdAt)
    }

    @Test
    fun `HabitEntity toDomain handles empty or unknown frequencies gracefully`() {
        val entity = HabitEntity(
            id = 1L,
            name = "Test",
            iconId = "unknown_icon",
            frequency = "MONDAY,INVALID_DAY, ,",
            createdAt = LocalDate.of(2026, 1, 1).toEpochDay()
        )

        val domain = entity.toDomain()

        assertEquals(HabitIcon.RUN, domain.icon)
        assertEquals(setOf(DayOfWeek.MONDAY), domain.frequency)
    }

    @Test
    fun `Habit toEntity roundtrip preserves all values`() {
        val habit = Habit(
            id = 99L,
            name = "Code Practice",
            icon = HabitIcon.CODE,
            frequency = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
            createdAt = LocalDate.of(2026, 4, 15)
        )

        val entity = habit.toEntity()
        val convertedBack = entity.toDomain()

        assertEquals(habit.id, convertedBack.id)
        assertEquals(habit.name, convertedBack.name)
        assertEquals(habit.icon, convertedBack.icon)
        assertEquals(habit.frequency, convertedBack.frequency)
        assertEquals(habit.createdAt, convertedBack.createdAt)
    }

    @Test
    fun `HabitCompletionEntity and HabitCompletion roundtrip preserves values`() {
        val completion = HabitCompletion(
            id = 10L,
            habitId = 5L,
            date = LocalDate.of(2026, 4, 20)
        )

        val entity = completion.toEntity()
        val domain = entity.toDomain()

        assertEquals(completion.id, domain.id)
        assertEquals(completion.habitId, domain.habitId)
        assertEquals(completion.date, domain.date)
    }
}
