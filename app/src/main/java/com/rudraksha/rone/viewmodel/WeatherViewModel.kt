package com.rudraksha.rone.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.rone.model.OneCallResponse
import com.rudraksha.rone.model.WeatherResponse
import com.rudraksha.rone.repo.CityCoordinates
import com.rudraksha.rone.repo.CityRepository
import com.rudraksha.rone.repo.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val weatherResponse = WeatherRepository.api.getWeatherByCity(city)
                _uiState.value = _uiState.value.copy(
                    weatherData = weatherResponse,
                    isLoading = false,
                    errorMessage = ""
                )
                Log.d("Temp", weatherResponse.main.temp.toString())
                Log.d("Loc Cond", weatherResponse.weather[0].description)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "",
                    isLoading = false
                )
                Log.d("Error", e.message.toString())
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            Log.d("LatLon", "Entered")
            try {
                val oneCallResponse = WeatherRepository.api.getWeatherByLatLon(
                    latitude = lat,
                    longitude = lon
                )
                Log.d("Temp", oneCallResponse.current.temp.toString())
                /*oneCallResponse.current.weather.forEach {
                    Log.d("Loc Cond", it.description)
                }
                oneCallResponse.hourly.forEach {
                    Log.d("Hourly", it.temp.toString() )
                }
                oneCallResponse.daily.forEach {
                    Log.d("Daily", it.temp.max.toString() + " " + it.temp.min)
                }*/

                _uiState.value = _uiState.value.copy(
                    oneCallResponse = oneCallResponse,
                    isLoading = false,
                    errorMessage = ""
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "",
                    isLoading = false
                )
                Log.d("Error", e.message.toString())
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun fetchCitySuggestions(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                _uiState.value = _uiState.value.copy(
                    cityCoordinates = CityRepository.api.getCitySuggestions(query)
                )
                /*_uiState.value.cityCoordinates.forEach{ it ->
                    Log.d("City", "${it.name}, ${it.lat}, ${it.lon}, ${it.country}")
                }*/
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "",
                    isLoading = false
                )
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}

data class WeatherUiState (
    var weatherData: WeatherResponse = WeatherResponse(),
    var oneCallResponse: OneCallResponse = OneCallResponse(),
    var isLoading: Boolean = false,
    var errorMessage: String = "",
    var cityCoordinates: List<CityCoordinates> = listOf(),
)
