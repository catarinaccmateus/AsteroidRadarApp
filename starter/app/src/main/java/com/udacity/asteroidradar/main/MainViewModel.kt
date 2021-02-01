package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.data.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidApiStatus
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.util.*

enum class TimeFrame {TODAY, WEEK, ALL}

class MainViewModel (application: Application) : AndroidViewModel(application) {

    /** Creating a database where we will retreive and add data */
    private val database = getDatabase(application)


    /** Creating a repository that will fetch data from network and save it to database
     * and will also allow to get data from database */
    private val repository = AsteroidsRepository(database)


    /** Data variables */
    private val timeFrame = MutableLiveData(TimeFrame.ALL)
    val asteroidsData = Transformations.map(timeFrame) { time ->
        time?.let {
            when (time) {
                TimeFrame.ALL -> repository.asteroids
                TimeFrame.TODAY -> repository.todayAsteroids
                TimeFrame.WEEK -> repository.weekAsteroids
            }
        }
    }


    val pictureOfDay = repository.pictureOfDay


    /** API Request Status*/
    val statusListAsteroids =  repository.apiStatus
    val statusPictureDay =  repository.apiPictureStatus

    /** Navigation status */
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail


    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
        }
    }


    fun onAsteroidItemClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun onAsteroidDetailNavigated() {
        _navigateToAsteroidDetail.value = null
    }


    fun getAsteroidsToday() {
       timeFrame.value = TimeFrame.TODAY
    }

    fun getAsteroidsWeek() {
        timeFrame.value = TimeFrame.WEEK
    }

    fun getAllAsteroids() {
        timeFrame.value = TimeFrame.ALL
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