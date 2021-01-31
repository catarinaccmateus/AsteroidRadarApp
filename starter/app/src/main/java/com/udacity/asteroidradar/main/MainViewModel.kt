package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.launch
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

enum class AsteroidApiStatus { LOADING, ERROR, DONE }



class MainViewModel : ViewModel() {

    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail

    private val _statusListAsteroids = MutableLiveData<AsteroidApiStatus>()
    val statusListAsteroids: LiveData<AsteroidApiStatus>
        get() = _statusListAsteroids

    private val _statusPictureDay = MutableLiveData<AsteroidApiStatus>()
    val statusPictureDay: LiveData<AsteroidApiStatus>
        get() = _statusPictureDay

    private val _asteroidsData = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidsData: LiveData<ArrayList<Asteroid>>
        get() = _asteroidsData

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    init {
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
        viewModelScope.launch {
            _statusPictureDay.value = AsteroidApiStatus.LOADING
            try {
              val response = NasaApi.retrofitPictureService.getPictureOfTheDay(Constants.APIKey)
                response?.let {
                    _pictureOfTheDay.value = it
                }
                _statusPictureDay.value = AsteroidApiStatus.DONE

            } catch (e: Exception) {
                _statusPictureDay.value = AsteroidApiStatus.ERROR

            }
        }
    }

    private fun getAsteroids() {
        /** Getting current date */
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        /** API Request */
        viewModelScope.launch {
            _statusListAsteroids.value = AsteroidApiStatus.LOADING
            try {
               val response =  NasaApi.retrofitAsteroidsService.getAllAsteroids(dateFormat.format(currentTime), Constants.APIKey)
                response?.let {
                    val jsonResponse = JSONObject(it)
                    val data = parseAsteroidsJsonResult(jsonResponse)
                    _asteroidsData.value = data
                }
                _statusListAsteroids.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _statusListAsteroids.value = AsteroidApiStatus.ERROR
            }
        }

    }
}