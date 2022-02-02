package com.moon.astromicon.models

import com.moon.astromicon.R

enum class MoonPhases(
    val icon: Int,
    val title: String
) {
    FULLMOON(R.drawable.moon_full_moon, "Полнолуние"),
    NEWMOON(R.drawable.moon_new_moon, "Новолуние"),
    WAXING_CRESCENT(R.drawable.moon_p, "Растущая луна"),
    WANING_MOON(R.drawable.moon_c, "Стареющая луна");

}