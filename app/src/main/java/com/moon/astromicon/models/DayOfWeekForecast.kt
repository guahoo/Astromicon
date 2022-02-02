package com.moon.astromicon.models

import com.moon.astromicon.R
import java.time.DayOfWeek

class DayOfWeekForecast() {

    fun getForecast(dayOfWeek: DayOfWeek): Int {

        return when (dayOfWeek) {

            DayOfWeek.MONDAY -> R.string.monday_forecast
            DayOfWeek.TUESDAY -> R.string.tuesday_forecast
            DayOfWeek.WEDNESDAY -> R.string.wednesday_forecast
            DayOfWeek.THURSDAY -> R.string.thursday_forecast
            DayOfWeek.FRIDAY -> R.string.friday_forecast
            DayOfWeek.SATURDAY -> R.string.saturday_forecast
            DayOfWeek.SUNDAY -> R.string.sunday_forecast

        }

    }

}