package com.moon.astromicon.models

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import java.time.ZonedDateTime

data class MoonModel(
    val date: LocalDateTime,
    val moonPhase: MoonIllumination,
    val fraction: Double,
    val moonRise: ZonedDateTime,
    val moonSet: ZonedDateTime,
    val moonDay: Long
)
