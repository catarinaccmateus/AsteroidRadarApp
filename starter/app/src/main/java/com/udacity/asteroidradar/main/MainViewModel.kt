package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.APIKey
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/* For testing purposes */
val listOfAsteroids: List<Asteroid> = listOf(
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, false),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true)
    )


class MainViewModel : ViewModel() {
    val data = MutableLiveData<List<Asteroid>?>()

    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail

    private val _apiResponseError = MutableLiveData<Boolean>()
    val apiResponseError: LiveData<Boolean>
        get() = _apiResponseError

    private val _asteroidsData = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidsData: LiveData<ArrayList<Asteroid>>
        get() = _asteroidsData

    init {
        data.value = listOfAsteroids
        _apiResponseError.value = false
    }

    fun onAsteroidItemClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }

    fun getAsteroids() {
        /** Getting current date */
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        /** API Request */
        NasaApi.retrofitService.getAllAsteroids(dateFormat.format(currentTime),  APIKey)
            .enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body() !== null) {
                    val jsonResponse = JSONObject(response.body())
                    val data = parseAsteroidsJsonResult(jsonResponse)
                    _asteroidsData.value = data
                }
                _apiResponseError.value = false
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _apiResponseError.value = true
            }

        })
    }
}