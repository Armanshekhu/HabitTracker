package com.plcoding.habittracker.ui.navigation

sealed class Screen(val route: String) {
    data object Today : Screen("today")
    data object Stats : Screen("stats")
    data object CreateHabit : Screen("create_habit")
    data object EditHabit : Screen("edit_habit/{habitId}") {
        fun createRoute(habitId: Long): String = "edit_habit/$habitId"
    }
}
