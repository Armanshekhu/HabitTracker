package com.plcoding.habittracker.ui.presentation.create_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.habittracker.domain.model.Habit
import com.plcoding.habittracker.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CreateEditHabitViewModel @Inject constructor(
    private val repository: HabitRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CreateEditHabitState())
    val state: StateFlow<CreateEditHabitState> = _state.asStateFlow()

    init {
        val habitIdString = savedStateHandle.get<String>("habitId")
        val habitId = habitIdString?.toLongOrNull()?.takeIf { it > 0 }
        if (habitId != null) {
            viewModelScope.launch {
                val existing = repository.getHabitById(habitId).firstOrNull()
                if (existing != null) {
                    _state.update {
                        it.copy(
                            habitId = existing.id,
                            name = existing.name,
                            selectedIcon = existing.icon,
                            selectedDays = existing.frequency,
                            createdAt = existing.createdAt
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: CreateEditHabitEvent) {
        when (event) {
            is CreateEditHabitEvent.NameChanged -> {
                val trimmedName = if (event.name.length > 50) event.name.take(50) else event.name
                _state.update {
                    it.copy(
                        name = trimmedName,
                        nameError = if (trimmedName.isNotBlank()) null else it.nameError
                    )
                }
            }

            is CreateEditHabitEvent.IconSelected -> {
                _state.update { it.copy(selectedIcon = event.icon) }
            }

            is CreateEditHabitEvent.ToggleDay -> {
                _state.update { current ->
                    val updated = if (current.selectedDays.contains(event.day)) {
                        current.selectedDays - event.day
                    } else {
                        current.selectedDays + event.day
                    }
                    current.copy(
                        selectedDays = updated,
                        daysError = if (updated.isNotEmpty()) null else current.daysError
                    )
                }
            }

            is CreateEditHabitEvent.SaveHabit -> {
                val current = _state.value
                val isNameEmpty = current.name.trim().isEmpty()
                val isDaysEmpty = current.selectedDays.isEmpty()

                if (isNameEmpty || isDaysEmpty) {
                    _state.update {
                        it.copy(
                            nameError = if (isNameEmpty) "Name cannot be empty" else null,
                            daysError = if (isDaysEmpty) "Select at least one day" else null
                        )
                    }
                    return
                }

                viewModelScope.launch {
                    if (current.isEditMode) {
                        repository.updateHabit(
                            Habit(
                                id = current.habitId!!,
                                name = current.name.trim(),
                                icon = current.selectedIcon,
                                frequency = current.selectedDays,
                                createdAt = current.createdAt
                            )
                        )
                    } else {
                        repository.insertHabit(
                            Habit(
                                name = current.name.trim(),
                                icon = current.selectedIcon,
                                frequency = current.selectedDays,
                                createdAt = LocalDate.now()
                            )
                        )
                    }
                    _state.update { it.copy(isSaved = true) }
                }
            }

            is CreateEditHabitEvent.ShowDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = event.show) }
            }

            is CreateEditHabitEvent.DeleteHabit -> {
                val habitId = _state.value.habitId ?: return
                viewModelScope.launch {
                    repository.deleteHabit(habitId)
                    _state.update { it.copy(showDeleteDialog = false, isDeleted = true) }
                }
            }
        }
    }
}
