package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.Asteroid
import com.udacity.asteroidradar.data.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidApiStatus
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.util.*

///* For testing purposes */
//val listOfAsteroids: List<Asteroid> = listOf(
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, false),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true),
//    Asteroid(2939L, "asteroid1", "2020-11-02", 9399.9, 929202.2, 20202.2, 2020.22, true)
//    )


class MainViewModel (application: Application) : AndroidViewModel(application) {

    /** Creating a database where we will retreive and add data */
    private val database = getDatabase(application)


    /** Creating a repository that will fetch data from network and save it to database
     * and will also allow to get data from database */
    private val repository = AsteroidsRepository(database)


    /** Data variables */
    val asteroidsData: LiveData<ArrayList<Asteroid>>
        get() = repository.asteroids


    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay


    /** API Request Status*/
    val statusListAsteroids: LiveData<AsteroidApiStatus>
        get() = repository.apiStatus

    private val _statusPictureDay = MutableLiveData<AsteroidApiStatus>()
    val statusPictureDay: LiveData<AsteroidApiStatus>
        get() = _statusPictureDay


    /** Navigation status */
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail


    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
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
                response.let {
                    _pictureOfTheDay.value = it
                }
                _statusPictureDay.value = AsteroidApiStatus.DONE

            } catch (e: Exception) {
                _statusPictureDay.value = AsteroidApiStatus.ERROR

            }
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct MainViewModel")
        }

    }
}