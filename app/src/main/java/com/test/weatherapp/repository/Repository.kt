package com.test.weatherapp.repository

import com.test.weatherapp.api.RetrofitInstance
import com.test.weatherapp.model.WeatherLocation
import retrofit2.Response

class Repository {
    suspend fun getWeather(apiKey: String, query: String): Response<WeatherLocation> {
        return RetrofitInstance.api.getWeather(apiKey, query)
    }
}