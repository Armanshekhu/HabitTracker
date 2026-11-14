package com.plcoding.habittracker.ui.presentation.create_edit

import com.plcoding.habittracker.domain.model.HabitIcon
import java.time.DayOfWeek
import java.time.LocalDate

data class CreateEditHabitState(
    val habitId: Long? = null,
    val name: String = "",
    val selectedIcon: HabitIcon = HabitIcon.RUN,
    val selectedDays: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
    val nameError: String? = null,
    val daysError: String? = null,
    val showDeleteDialog: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val createdAt: LocalDate = LocalDate.now()
) {
    val isEditMode: Boolean get() = habitId != null
}
