package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.data.NetworkPictureOfDay
import com.udacity.asteroidradar.data.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePictureOfDay

data class NetworkAsteroidContainer(val asteroids: ArrayList<Asteroid>)

/**
 * Converting a Network Object into a Database Object.
 */
fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

/**
 * Converting a Database Object into an Array of Asteroids
 */

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

/**
 * Network PictureOfTheDay Object to Database Object
 */

fun NetworkPictureOfDay.asDatabaseModel(): DatabasePictureOfDay{
    return  DatabasePictureOfDay(
        id = 0L,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}

/**
 * Database PictureOfTheDay Object to Database Object
 */

fun DatabasePictureOfDay.asDomainModel(): PictureOfDay{
    return  PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}