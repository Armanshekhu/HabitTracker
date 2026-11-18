package com.plcoding.habittracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.plcoding.habittracker.ui.presentation.create_edit.CreateEditHabitScreen
import com.plcoding.habittracker.ui.presentation.create_edit.CreateEditHabitViewModel
import com.plcoding.habittracker.ui.presentation.stats.StatsScreen
import com.plcoding.habittracker.ui.presentation.stats.StatsViewModel
import com.plcoding.habittracker.ui.presentation.today.TodayScreen
import com.plcoding.habittracker.ui.presentation.today.TodayViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Today.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Today.route) {
            val viewModel: TodayViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            TodayScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route)
                },
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateHabit.route)
                },
                onNavigateToEdit = { habitId ->
                    navController.navigate(Screen.EditHabit.createRoute(habitId))
                }
            )
        }

        composable(route = Screen.Stats.route) {
            val viewModel: StatsViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            StatsScreen(
                state = state,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.CreateHabit.route) {
            val viewModel: CreateEditHabitViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            CreateEditHabitScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditHabit.route,
            arguments = listOf(
                navArgument("habitId") {
                    type = NavType.StringType
                }
            )
        ) {
            val viewModel: CreateEditHabitViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            CreateEditHabitScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
