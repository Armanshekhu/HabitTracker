package com.plcoding.habittracker.domain.util

import com.plcoding.habittracker.domain.model.Habit
import com.plcoding.habittracker.domain.model.HabitIcon
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class StreakCalculatorTest {

    @Test
    fun `when habit completed today, current streak includes today`() {
        val today = LocalDate.of(2026, 4, 6) // Monday
        val habit = Habit(
            id = 1,
            name = "Morning Run",
            icon = HabitIcon.RUN,
            frequency = DayOfWeek.entries.toSet(), // Everyday
            createdAt = today.minusDays(10)
        )
        val completedDates = setOf(
            today,
            today.minusDays(1),
            today.minusDays(2)
        )

        val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, today)
        assertEquals(3, currentStreak)
    }

    @Test
    fun `when habit not completed today, current streak counts from yesterday`() {
        val today = LocalDate.of(2026, 4, 6) // Monday
        val habit = Habit(
            id = 1,
            name = "Morning Run",
            icon = HabitIcon.RUN,
            frequency = DayOfWeek.entries.toSet(),
            createdAt = today.minusDays(10)
        )
        // Completed yesterday and day before, but not today yet
        val completedDates = setOf(
            today.minusDays(1),
            today.minusDays(2)
        )

        val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, today)
        assertEquals(2, currentStreak)
    }

    @Test
    fun `when scheduled day was missed yesterday, current streak is 0`() {
        val today = LocalDate.of(2026, 4, 6) // Monday
        val habit = Habit(
            id = 1,
            name = "Morning Run",
            icon = HabitIcon.RUN,
            frequency = DayOfWeek.entries.toSet(),
            createdAt = today.minusDays(10)
        )
        // Missed yesterday
        val completedDates = setOf(
            today.minusDays(2),
            today.minusDays(3)
        )

        val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, today)
        assertEquals(0, currentStreak)
    }

    @Test
    fun `non scheduled days do not break streak`() {
        // Weekday habit (Monday to Friday)
        val today = LocalDate.of(2026, 4, 6) // Monday
        val habit = Habit(
            id = 1,
            name = "Code",
            icon = HabitIcon.CODE,
            frequency = setOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
            ),
            createdAt = today.minusDays(14)
        )
        // Completed Monday (today), Friday, Thursday (Saturday & Sunday not scheduled)
        val completedDates = setOf(
            today, // Monday
            today.minusDays(3), // Friday
            today.minusDays(4) // Thursday
        )

        val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, today)
        assertEquals(3, currentStreak)
    }

    @Test
    fun `habit created mid-week does not count prior days as missed`() {
        val today = LocalDate.of(2026, 4, 6) // Monday
        val habit = Habit(
            id = 1,
            name = "Water",
            icon = HabitIcon.WATER,
            frequency = DayOfWeek.entries.toSet(),
            createdAt = today.minusDays(1) // Created yesterday
        )
        val completedDates = setOf(
            today,
            today.minusDays(1)
        )

        val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, today)
        assertEquals(2, currentStreak)
    }

    @Test
    fun `best streak calculates longest consecutive run across history`() {
        val today = LocalDate.of(2026, 4, 6)
        val habit = Habit(
            id = 1,
            name = "Read",
            icon = HabitIcon.READ,
            frequency = DayOfWeek.entries.toSet(),
            createdAt = today.minusDays(10)
        )
        // Past streak of 4 days, then missed a day, then current streak of 2 days
        val completedDates = setOf(
            today.minusDays(10),
            today.minusDays(9),
            today.minusDays(8),
            today.minusDays(7),
            // missed day 6, 5, 4, 3
            today.minusDays(1),
            today
        )

        val currentStreak = StreakCalculator.calculateCurrentStreak(habit, completedDates, today)
        val bestStreak = StreakCalculator.calculateBestStreak(habit, completedDates, today)

        assertEquals(2, currentStreak)
        assertEquals(4, bestStreak)
    }
}
