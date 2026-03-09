package com.example.simpleweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.data.CityEntity
import com.example.simpleweather.domain.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Empty)
    val state: StateFlow<WeatherState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _cityList = MutableStateFlow<List<CityEntity>>(emptyList())
    val cityList: StateFlow<List<CityEntity>> = _cityList

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun setCityList(cities: List<CityEntity>) {
        _cityList.value = cities
    }

    fun loadWeather(cityName: String = _searchText.value) {
        if (cityName.isBlank()) return

        viewModelScope.launch {
            _state.value = WeatherState.Loading
            try {
                // Заглушка для погоды (можно заменить на реальный вызов API)
                val weather = Weather(
                    city = cityName,
                    country = "RU",
                    temperature = (0..30).random().toDouble(),
                    description = "Солнечно",
                    humidity = (30..80).random(),
                    iconUrl = "01d"
                )
                _state.value = WeatherState.Success(weather)
            } catch (e: Exception) {
                _state.value = WeatherState.Error(e.message ?: "Ошибка загрузки погоды")
            }
        }
    }

    fun getFilteredCities(): List<CityEntity> {
        val query = _searchText.value.trim()
        return if (query.isEmpty()) emptyList()
        else _cityList.value.filter { it.name.contains(query, ignoreCase = true) }
    }
}