package com.example.simpleweather.data

import com.example.simpleweather.domain.Weather
import kotlinx.coroutines.delay

class WeatherRepository(private val api: WeatherApi) {
    suspend fun getWeather(city: String): Result<Weather> {
        return try {

            delay(1000)

            val response = api.getWeather(city)
            val weather = Weather(
                city = response.name,
                country = response.sys.country,
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "",
                humidity = response.main.humidity,
                iconUrl = response.weather.firstOrNull()?.icon ?: ""
            )
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}