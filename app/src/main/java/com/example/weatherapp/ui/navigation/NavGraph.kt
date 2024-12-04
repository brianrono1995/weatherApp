package com.example.weatherapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.ui.screens.DetailsScreen
import com.example.weatherapp.ui.screens.HomeScreen
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel: WeatherViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onDateSelected = { selectedDate, selectedCity ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedCity", selectedCity)
                    navController.navigate("details/$selectedDate")
                }
            )
        }

        composable("details/{date}") { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("date") ?: ""
            val selectedCity = backStackEntry.savedStateHandle.get<String>("selectedCity") ?: "Nairobi"
            DetailsScreen(
                date = selectedDate,
                city = selectedCity,
                viewModel = viewModel
            )
        }
    }
}
