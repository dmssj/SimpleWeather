package com.example.simpleweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.data.weatherApi
import com.example.simpleweather.domain.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {


    private val _state = MutableStateFlow<WeatherState>(WeatherState.Empty)
    val state: StateFlow<WeatherState> = _state

    private val _searchText = MutableStateFlow("Москва")
    val searchText: StateFlow<String> = _searchText

    init {
        loadWeather()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    fun loadWeather() {
        val city = _searchText.value
        if (city.isBlank()) return

        viewModelScope.launch {
            _state.value = WeatherState.Loading

            try {
                val response = weatherApi.getWeather(city)
                val weather = Weather(
                    city = response.name,
                    country = response.sys.country,
                    temperature = response.main.temp,
                    description = response.weather.firstOrNull()?.description ?: "",
                    humidity = response.main.humidity,
                    iconUrl = response.weather.firstOrNull()?.icon ?: "01d"
                )
                _state.value = WeatherState.Success(weather)
            } catch (e: Exception) {
                _state.value = WeatherState.Error(e.message ?: "Ошибка сети")
            }
        }
    }
}