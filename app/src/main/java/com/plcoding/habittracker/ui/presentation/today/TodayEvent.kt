package com.plcoding.habittracker.ui.presentation.today

sealed interface TodayEvent {
    data class ToggleCompletion(val habitId: Long) : TodayEvent
}
