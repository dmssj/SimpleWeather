package com.example.simpleweather.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "1fa522bf0dd3d657c5ab21ddb4267cfb",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru"
    ): WeatherResponse
}

data class WeatherResponse(
    val main: MainData,
    val weather: List<WeatherData>,
    val name: String,
    val sys: SysData
)

data class MainData(
    val temp: Double,
    val humidity: Int
)

data class WeatherData(
    val description: String,
    val icon: String
)

data class SysData(
    val country: String
)


val weatherApi: WeatherApi by lazy {
    Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)
}