package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.api.HourlyWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    // Fetch weather data for a specific location and date
    fun fetchWeather(location: String, date: String, apiKey: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val weatherData = repository.getWeatherData(location, date, apiKey)
                _uiState.value = _uiState.value.copy(
                    hourlyWeather = weatherData,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to fetch weather data: ${e.localizedMessage}"
                )
            }
        }
    }
}

// Data class for representing the UI state
data class WeatherUiState(
    val hourlyWeather: List<HourlyWeather> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
