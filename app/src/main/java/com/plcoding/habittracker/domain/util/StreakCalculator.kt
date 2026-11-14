package com.plcoding.habittracker.domain.util

import com.plcoding.habittracker.domain.model.Habit
import java.time.LocalDate

object StreakCalculator {

    /**
     * Calculates the current streak and best streak for a habit.
     */
    fun calculateCurrentStreak(
        habit: Habit,
        completedDates: Set<LocalDate>,
        today: LocalDate = LocalDate.now()
    ): Int {
        if (habit.frequency.isEmpty() || today.isBefore(habit.createdAt)) {
            return 0
        }

        var streak = 0
        val isTodayScheduled = habit.frequency.contains(today.dayOfWeek) && !today.isBefore(habit.createdAt)
        val isTodayCompleted = isTodayScheduled && completedDates.contains(today)

        var checkDate = if (isTodayScheduled) {
            if (isTodayCompleted) {
                streak++
                today.minusDays(1)
            } else {
                // Today is scheduled but not completed yet; don't break streak yet, count from yesterday
                today.minusDays(1)
            }
        } else {
            // Today is not scheduled, start checking backwards from yesterday
            today.minusDays(1)
        }

        // Walk backwards through past days up to createdAt
        while (!checkDate.isBefore(habit.createdAt)) {
            if (habit.frequency.contains(checkDate.dayOfWeek)) {
                if (completedDates.contains(checkDate)) {
                    streak++
                } else {
                    // Missed a scheduled day; streak ends
                    break
                }
            }
            checkDate = checkDate.minusDays(1)
        }

        return streak
    }

    /**
     * Calculates the best (longest) streak recorded from habit.createdAt up to today.
     */
    fun calculateBestStreak(
        habit: Habit,
        completedDates: Set<LocalDate>,
        today: LocalDate = LocalDate.now()
    ): Int {
        if (habit.frequency.isEmpty() || today.isBefore(habit.createdAt)) {
            return 0
        }

        var maxStreak = 0
        var currentRun = 0

        var date = habit.createdAt
        while (!date.isAfter(today)) {
            if (habit.frequency.contains(date.dayOfWeek)) {
                if (completedDates.contains(date)) {
                    currentRun++
                    if (currentRun > maxStreak) {
                        maxStreak = currentRun
                    }
                } else {
                    currentRun = 0
                }
            }
            date = date.plusDays(1)
        }

        val currentStreak = calculateCurrentStreak(habit, completedDates, today)
        return maxOf(maxStreak, currentStreak)
    }
}
