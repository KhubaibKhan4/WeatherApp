package com.test.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.weatherapp.model.WeatherLocation
import com.test.weatherapp.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<WeatherLocation>> = MutableLiveData()

    fun getWeather(apiKey: String, query: String) {
        viewModelScope.launch {
            val response = repository.getWeather(apiKey, query)
            myResponse.value = response
        }
    }

}