package com.udacity.asteroidradar

import com.udacity.asteroidradar.utils.Constants
import java.text.SimpleDateFormat
import java.util.*


fun getCurrentDateAsString(): String {
    /** Getting current date */
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}

/** Function to get asteroids from next week */
fun getNextWeekDayString(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(calendar.time)
}