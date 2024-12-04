package com.example.weatherapp.data.database

import androidx.room.*

@Dao
interface WeatherDao {

    /**
     * Retrieve weather data for a specific date.
     * @param date The date for which weather data is requested.
     * @return The cached WeatherEntity for the given date, or null if not found.
     */
    @Query("SELECT * FROM weather WHERE date = :date")
    suspend fun getWeatherByDate(date: String): WeatherEntity?

    /**
     * Insert weather data into the database.
     * If the data for the same date already exists, it will be replaced.
     * @param weather The WeatherEntity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    /**
     * Delete all weather data from the database.
     */
    @Query("DELETE FROM weather")
    suspend fun clearAllWeatherData()
}
