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

fun getLastWeekDayString(): String {
    val calendar = Calendar.getInstance()
    val currentDay = calendar[Calendar.DAY_OF_WEEK]
    val leftDays = Calendar.SUNDAY - currentDay
    calendar.add(Calendar.DATE, leftDays)
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    /** Help - when filtering it doesnt show any asteroids for the current week, even though it exists.
     *  Tried to log this date unsuccessfully */
    return dateFormat.format(calendar.time)
}