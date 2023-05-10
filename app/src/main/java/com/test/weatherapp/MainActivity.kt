package com.test.weatherapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.test.weatherapp.databinding.ActivityMainBinding
import com.test.weatherapp.repository.Repository


class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    private var lat: Double = 0.00
    private var lon: Double = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait....")
        progressDialog.setMessage("Your Data is Being Loaded from the Server. Please Be Patient...")
        progressDialog.setCancelable(false)
        progressDialog.show()




        val repository = Repository()
        val mainViewModelFactory = MainViewModelFactory(repository)

        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.getWeather("apikey", query.toString())
                progressDialog.dismiss()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                progressDialog.dismiss()
                return false
            }

        })


        mainViewModel.getWeather("apikey", "multan")
        mainViewModel.myResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                progressDialog.dismiss()
                binding.locationTextView.text =
                    response.body()?.location?.name + ", " + response.body()?.location?.region + ", " + response.body()?.location?.country
                binding.lastUpdatedTextView.text =
                    "Last Updated: " + response.body()?.current?.last_updated

                Glide.with(this)
                    .load(response.body()?.current?.condition?.icon.toString())
                    .placeholder(R.drawable.ic_weather).into(binding.weatherIconImageView)


                binding.temperatureTextView.text = response.body()?.current?.temp_c.toString()
                binding.weatherConditionTextView.text = response.body()?.current?.condition?.text
                binding.windSpeedTextView.text =
                    "Wind: " + response.body()?.current?.wind_kph.toString() + "kph,"
                binding.pressureTextView.text =
                    "Pressure: " + response.body()?.current?.pressure_mb.toString() + "mb,"
                binding.humidityTextView.text =
                    "Humidity: " + response.body()?.current?.humidity.toString() + "%"

                binding.cloudTextView.text =
                    "Cloud: " + response.body()?.current?.cloud.toString() + "%"
                binding.feelsLikeTextView.text =
                    "Feels Like: " + response.body()?.current?.feelslike_f.toString()+"°F"
                binding.visibilityTextView.text =
                    "Visibility: " + response.body()?.current?.vis_km.toString()+" km"
                binding.uvIndexTextView.text =
                    "UV Index: " + response.body()?.current?.uv.toString()


                //Log Response
                Log.d("Response", response.body()?.location?.country.toString())
                Log.d("Response", response.body()?.location?.localtime.toString())
                Log.d("Response", response.body()?.location?.name.toString())
                Log.d("Response", response.body()?.location?.region.toString())
                Log.d("Response", response.body()?.location?.lat.toString())
                Log.d("Response", response.body()?.location?.lon.toString())
                Log.d("Response", response.body()?.location?.localtime.toString())
                Log.d("Response", response.body()?.location?.localtime_epoch.toString())
                Log.d("Response", response.body()?.current?.condition?.icon.toString())
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "${response.errorBody()}", Toast.LENGTH_LONG).show()
            }
        })
    }
}