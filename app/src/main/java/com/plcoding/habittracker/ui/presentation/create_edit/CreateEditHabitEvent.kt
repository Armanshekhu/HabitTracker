package com.plcoding.habittracker.ui.presentation.create_edit

import com.plcoding.habittracker.domain.model.HabitIcon
import java.time.DayOfWeek

sealed interface CreateEditHabitEvent {
    data class NameChanged(val name: String) : CreateEditHabitEvent
    data class IconSelected(val icon: HabitIcon) : CreateEditHabitEvent
    data class ToggleDay(val day: DayOfWeek) : CreateEditHabitEvent
    data object SaveHabit : CreateEditHabitEvent
    data class ShowDeleteDialog(val show: Boolean) : CreateEditHabitEvent
    data object DeleteHabit : CreateEditHabitEvent
}
