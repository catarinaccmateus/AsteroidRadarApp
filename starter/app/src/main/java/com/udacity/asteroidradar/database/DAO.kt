package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface AsteroidsDao {
    /* Getting Asteroids */
    @Query("select * from asteroid_list")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun instertAll(vararg asteroids: DatabaseAsteroid)
    @Query("select * from asteroid_list where closeApproachDate >= date('now') order by closeApproachDate asc ")
    fun getAllAsteroidsFromToday(): LiveData<List<DatabaseAsteroid>>
    @Query("select * from asteroid_list where closeApproachDate == date('now') ")
    fun getAllAsteroidsToday(): LiveData<List<DatabaseAsteroid>>
    @Query("select * from asteroid_list where closeApproachDate between date('now') and :toDate order by closeApproachDate asc ")
    fun getWeekAsteroids(toDate: String): LiveData<List<DatabaseAsteroid>>
    @Query("delete from asteroid_list where closeApproachDate < date('now')")
    fun deleteAsteroidsPreviousDays()

    /* Getting picture of the day */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDay: DatabasePictureOfDay)
    @Query("select * from picture_of_day limit 1")
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>
}