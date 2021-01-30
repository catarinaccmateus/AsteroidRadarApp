package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.Constants
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

///* For testing purposes */
//val listOfAsteroids: List<Asteroid> = listOf(
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, false),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true)
//    )


class MainViewModel : ViewModel() {

    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail

    private val _apiResponseError = MutableLiveData<Boolean>()
    val apiResponseError: LiveData<Boolean>
        get() = _apiResponseError

    private val _asteroidsData = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidsData: LiveData<ArrayList<Asteroid>>
        get() = _asteroidsData

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    init {
        _apiResponseError.value = false
        getAsteroids()
        getPictureOfTheDay()
    }


    fun onAsteroidItemClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }

    private fun getPictureOfTheDay() {
    NasaApi.retrofitPictureService.getPictureOfTheDay(Constants.APIKey)
    .enqueue(object: Callback <PictureOfDay> {
        override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
            if (response.body() !== null) {
                _pictureOfTheDay.value = response.body()
            }
            _apiResponseError.value = false
        }

        override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
            _apiResponseError.value = true
        }

    })
    }

    private fun getAsteroids() {
        /** Getting current date */
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        /** API Request */
        NasaApi.retrofitAsteroidsService.getAllAsteroids(dateFormat.format(currentTime), Constants.APIKey)
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