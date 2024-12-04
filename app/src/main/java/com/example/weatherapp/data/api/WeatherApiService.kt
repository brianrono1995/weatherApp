package com.example.weatherapp.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/history.json")
    suspend fun getHistoricalWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("dt") date: String
    ): WeatherResponse
}

// Example of WeatherResponse (you'll expand it later based on the API's JSON response)
data class WeatherResponse(
    val location: Location,
    val forecast: Forecast
)

data class Location(val name: String, val country: String)
data class Forecast(val forecastday: List<ForecastDay>)

data class ForecastDay(
    val date: String,
    val hour: List<HourlyWeather>
)

data class HourlyWeather(
    val time: String,
    val temp_c: Double,
    val temp_f: Double,
    val humidity: Int,
    val wind_kph: Double,
    val condition: Condition
)

data class Condition(val text: String, val icon: String)
