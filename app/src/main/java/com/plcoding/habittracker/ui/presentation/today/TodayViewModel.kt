package com.plcoding.habittracker.ui.presentation.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.habittracker.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TodayState())
    val state: StateFlow<TodayState> = _state.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        val today = LocalDate.now()
        repository.getHabitsWithStreaks(today)
            .onEach { allHabits ->
                val scheduledToday = allHabits.filter { item ->
                    item.habit.frequency.contains(today.dayOfWeek) &&
                            !today.isBefore(item.habit.createdAt)
                }
                val completed = scheduledToday.count { it.isCompletedToday }

                _state.update {
                    it.copy(
                        date = today,
                        habits = scheduledToday,
                        completedCount = completed,
                        totalCount = scheduledToday.size,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: TodayEvent) {
        when (event) {
            is TodayEvent.ToggleCompletion -> {
                viewModelScope.launch {
                    repository.toggleCompletion(event.habitId, _state.value.date)
                }
            }
        }
    }
}
