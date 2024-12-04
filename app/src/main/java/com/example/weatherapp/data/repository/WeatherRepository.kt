package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.WeatherApiService
import com.example.weatherapp.data.api.HourlyWeather
import com.example.weatherapp.data.database.WeatherDao
import com.example.weatherapp.data.database.WeatherEntity
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService,
    private val weatherDao: WeatherDao
) {

    /**
     * Fetch weather data for a specific location and date.
     * @param location The location for which weather data is requested (e.g., "Nairobi").
     * @param date The date for which weather data is requested (format: "YYYY-MM-DD").
     * @param apiKey The API key for accessing the weather API.
     * @return A list of hourly weather data.
     * @throws Exception if both API and cache retrieval fail.
     */
    suspend fun getWeatherData(location: String, date: String, apiKey: String): List<HourlyWeather> {
        return try {
            // Check if data for the given date exists in the local cache
            val cachedData = weatherDao.getWeatherByDate(date)
            if (cachedData != null) {
                return cachedData.hourlyWeather
            }

            // Fetch data from the API if not cached
            val response = apiService.getHistoricalWeather(apiKey, location, date)
            val hourlyWeather = response.forecast.forecastday.firstOrNull()?.hour
                ?: throw Exception("No forecast data available for the requested date.")

            // Save the fetched data to the local database
            val weatherEntity = WeatherEntity(
                date = date,
                hourlyWeather = hourlyWeather
            )
            weatherDao.insertWeather(weatherEntity)

            hourlyWeather
        } catch (e: Exception) {
            // Handle and log API or database errors
            throw Exception("Failed to fetch weather data: ${e.localizedMessage}")
        }
    }
}
