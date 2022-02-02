package com.moon.astromicon.moondata

import android.animation.ObjectAnimator
import android.view.View
import com.moon.astromicon.extensions.MathUtils
import com.moon.astromicon.models.Coordinates
import com.moon.astromicon.models.MoonModel
import com.moon.astromicon.models.ZodiacSign
import org.shredzone.commons.suncalc.MoonIllumination
import org.shredzone.commons.suncalc.MoonPhase
import org.shredzone.commons.suncalc.MoonTimes
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
import kotlin.jvm.internal.Intrinsics

class ComputeMoonPosition {

    fun setMoon(dateFromPicker: org.joda.time.LocalDateTime, coord: Coordinates): MoonModel {

        val moonMap = HashMap<Long, MoonModel>()

        val moonVisibility = MoonIllumination.compute()
            .on(dateFromPicker.year, dateFromPicker.monthOfYear, dateFromPicker.dayOfMonth)
            .execute()

        // animateMoon(moonVisibility)

        /**
         * рассвет крайнего дня по дейт пикеру
         */
        val dateLastRise: LocalDateTime = LocalDateTime.of(
            dateFromPicker.year,
            dateFromPicker.monthOfYear,
            dateFromPicker.dayOfMonth,
            0,
            0
        )

        val currentRise = MoonTimes.compute()
            .on(dateLastRise)
            .fullCycle()
            .at(coord.lat, coord.lon)
            .execute().rise

        val currentSet = MoonTimes.compute()
            .on(dateLastRise)
            .fullCycle()
            .at(coord.lat, coord.lon)
            .execute().set

        val date: LocalDateTime = LocalDateTime.of(
            dateFromPicker.year,
            dateFromPicker.monthOfYear,
            dateFromPicker.dayOfMonth,
            0,
            0
        )

        val moonPhase = MoonIllumination.compute()
            .on((currentRise))
            .timezone(TimeZone.getDefault())
            .execute()

        val moonFraction = MoonIllumination.compute()
            .on(currentRise)
            .timezone(TimeZone.getDefault())
            .execute().fraction

        var lunarDay = 1




        while (true) {

            val parameters = MoonPhase.compute()
                .phase(MoonPhase.Phase.NEW_MOON)
                .timezone(TimeZone.getDefault())
                .on(date.minusDays(29).minusHours(12))
                .execute()

            val firstRise = parameters.time

            val lastRise = MoonTimes.compute()
                .on(firstRise.plusMinutes(1488L * lunarDay))
                .at(coord.lat, coord.lon)
                .execute().rise

            println("Ласт райз ${lastRise} $currentRise")


            lunarDay++

            if (lastRise!!.plusMinutes(5).isAfter(currentRise!!)) {
                lunarDay++
                break
            }
        }












            return MoonModel(
                date = dateFromPicker,
                moonPhase = moonPhase,
                fraction = moonFraction,
                moonRise = currentRise!!,
                moonSet = currentSet!!,
                moonDay = lunarDay.toLong()
            )

//        var l = dateFromPicker.year - 2000
//
//        if (l > 0) {
//            while (l + 6 > 19) {
//                println("Число года1 ${l+6}")
//                l = l + 6 - 19
//                println("Число года $l")
//            }
//        } else {
//            while ((2000 - l) - 6 > 19) {
//                l += 19
//            }
//        }
//
//            var n = (l * 11) - 14
//        println("Лунное число года $n $l")
////
//            while (n > 30) {
//                n -= 30
//            }
//        println("Лунное число года1 $n")
//
//        n += dateFromPicker.dayOfMonth + dateFromPicker.monthOfYear
//
//        println("Лунное число года2 $n")
//
//        while (n > 30) {
//            n -= 30
//        }
//
//        println("Лунное число года3 $n")

//        /**
//         * расчет первого восхода луны после Новолуния
//         */
//
//
//          //  .minusMinutes(42524)
//

//
//        val params = MoonPhase.compute()
//            .phase(MoonPhase.Phase.NEW_MOON)
//            .timezone(TimeZone.getDefault())
//            .on(
//                LocalDateTime.of(
//                    dateFromPicker.year,
//                    dateFromPicker.monthOfYear,
//                    dateFromPicker.dayOfMonth,
//                    0,
//                    0
//                )
//            )
//            .execute()
//
//        val newMoonRise = params.time
//
//        val parameters2 = MoonPhase.compute()
//            .phase(MoonPhase.Phase.NEW_MOON)
//            .timezone(TimeZone.getDefault())
//            .on(
//                LocalDateTime.of(
//                    dateFromPicker.year,
//                    dateFromPicker.monthOfYear,
//                    dateFromPicker.dayOfMonth,
//                    0,
//                    0
//                )
//            )
//            .execute()
//
//        val firstRise2 = parameters2.time
//        //.plusMinutes(720)
//
//// 24 часам и 48 минутам
//        /**
//         * построение лунной таблицы
//         */
//        while (true) {
//
//            var computeTime = firstRise.plusMinutes(1488 * lunarconst)
//            var computeCoord = coord
//
//            val moonRise = MoonTimes.compute()
//                .on(computeTime)
//                .fullCycle()
//                .at(computeCoord.lat, computeCoord.lon)
//                .execute().rise
//            val dateLastRise = java.time.LocalDateTime.of(
//                dateFromPicker.year,
//                dateFromPicker.monthOfYear,
//                dateFromPicker.dayOfMonth,
//                0,
//                0
//            )
//
//            MoonPosition.compute()
//                .on(dateLastRise)
//                .at(computeCoord.lat, computeCoord.lon)
//                .utc()
//
//            val moonSet = MoonTimes.compute()
//                .on(firstRise.plusMinutes(1488 * lunarconst))
//                .fullCycle()
//                .at(computeCoord.lat, computeCoord.lon)
//                .execute().set!!
//

//
//
//
//
//
//
//
//            if (lunarconst == 0L) {
//
//                moonMap[trueLunarDay] = MoonModel(
//                    date = dateFromPicker,
//                    moonPhase = moonPhase,
//                    fraction = moonFraction,
//                    moonRise = firstRise,
//                    moonSet = moonSet,
//                    moonDay = trueLunarDay
//                )
//                trueLunarDay++
//            }
//           println("MoonMap $moonRise ${currentRise}")
//            if (moonRise!!.plusHours(1L).isAfter(currentRise)) {
//                break
//            } else if (trueLunarDay > 28 && currentRise!!.isAfter(firstRise2)) {
//                println("Трулунардэй $trueLunarDay")
//                moonMap[trueLunarDay] = MoonModel(
//                    date = dateFromPicker,
//                    moonPhase = moonPhase,
//                    fraction = moonFraction,
//                    moonRise = moonRise!!,
//                    moonSet = moonSet,
//                    moonDay = 1
//                )
//                // trueLunarDay = 1L
//                break
//            } else {
//                moonMap[trueLunarDay] = MoonModel(
//                    date = dateFromPicker,
//                    moonPhase = moonPhase,
//                    fraction = moonFraction,
//                    moonRise = moonRise!!,
//                    moonSet = moonSet,
//                    moonDay = trueLunarDay + 1
//                )
//            }
//            lunarconst++
//            trueLunarDay++
//
//        }

        }

        fun getZodiacSign(_date: org.threeten.bp.LocalDate): ZodiacSign {
            Intrinsics.checkParameterIsNotNull(_date, "_date")
            var longitude = 0.0
            var yy = 0.0
            var mm = 0.0
            var k1 = 0.0
            var k2 = 0.0
            var k3 = 0.0
            var jd = 0.0
            var ip = 0.0
            var dp = 0.0
            var rp = 0.0
            val year = _date.year
            val month = _date.monthValue
            val day = _date.dayOfMonth
            var var10000 = year.toDouble()
            var var25 = (12.0 - month.toDouble()) / 10.0
            var var28 = var10000
            var var27 = false
            var var30 = Math.floor(var25)
            yy = var28 - var30
            mm = month.toDouble() + 9.0
            if (mm >= 12.toDouble()) {
                mm -= 12.toDouble()
            }
            var25 = 365.25 * (yy + 4712.toDouble())
            var27 = false
            k1 = Math.floor(var25)
            var25 = 30.6 * mm + 0.5
            var27 = false
            k2 = Math.floor(var25)
            var25 = yy / 100.toDouble() + 49.toDouble()
            var27 = false
            var25 = Math.floor(var25) * 0.75
            var27 = false
            k3 = Math.floor(var25) - 38.toDouble()
            jd = k1 + k2 + day.toDouble() + 59.toDouble()
            if (jd > 2299160.toDouble()) {
                jd -= k3
            }
            ip = MathUtils.INSTANCE!!.normalize((jd - 2451550.1) / 29.530588853)
            ip *= 6.283185307179586
            dp = 6.283185307179586 * MathUtils.INSTANCE!!.normalize((jd - 2451562.2) / 27.55454988)
            rp = MathUtils.INSTANCE!!.normalize((jd - 2451555.8) / 27.321582241)
            var10000 = 360.toDouble() * rp
            var30 = 6.3
            var28 = var10000
            val var34 = false
            var var32 = Math.sin(dp)
            var10000 = var28 + var30 * var32
            var25 = 2.toDouble() * ip - dp
            var30 = 1.3
            var28 = var10000
            var27 = false
            var32 = Math.sin(var25)
            var10000 = var28 + var30 * var32
            var25 = 2.toDouble() * ip
            var30 = 0.7
            var28 = var10000
            var27 = false
            var32 = Math.sin(var25)
            longitude = var28 + var30 * var32
            return if (longitude < 33.18) ZodiacSign.ARIES else if (longitude < 51.16) ZodiacSign.CANCER else if (longitude < 93.44) ZodiacSign.GEMINI else if (longitude < 119.48) ZodiacSign.CANCER else if (longitude < 135.3) ZodiacSign.LEO else if (longitude < 173.34) ZodiacSign.VIRGO else if (longitude < 224.17) ZodiacSign.LIBRA else if (longitude < 242.57) ZodiacSign.SCORPIO else if (longitude < 271.26) ZodiacSign.SAGITTARIUS else if (longitude < 302.49) ZodiacSign.CAPRICORN else if (longitude < 311.72) ZodiacSign.AQUARIUS else if (longitude < 348.58) ZodiacSign.PISCES else ZodiacSign.ARIES
        }

        internal fun animateMoon(moonVisibility: MoonIllumination, black_moon: View) {
            /**
             * анимация луны
             */
            var x = 180 / moonVisibility.phase

            val animateTo =
                (if (moonVisibility.phase > 0) black_moon.width - (black_moon.width / x).toFloat()
                else -(black_moon.width + (black_moon.width / x).toFloat()))

            val buttonAnimator = ObjectAnimator.ofFloat(
                black_moon,
                "translationX", animateTo
            )
            buttonAnimator.duration = 500
            buttonAnimator.start()
        }

    }