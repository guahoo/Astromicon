package com.moon.astromicon.models

import com.moon.astromicon.R

enum class ZodiacSign {
    ARIES,
    TAURUS,
    CANCER,
    GEMINI,
    LEO,
    VIRGO,
    LIBRA,
    SCORPIO,
    CAPRICORN,
    AQUARIUS,
    SAGITTARIUS,
    PISCES;

     fun ordinal(sign: ZodiacSign): Int {

         return when(sign){
             ARIES -> R.drawable.zodiac_aries
             TAURUS -> R.drawable.zodiac_taurus
             CANCER -> R.drawable.zodiac_cancer
             GEMINI -> R.drawable.zodiac_gemini
             LEO -> R.drawable.zodiac_leo
             VIRGO -> R.drawable.zodiac_virgo
             LIBRA -> R.drawable.zodiac_libra
             SCORPIO -> R.drawable.zodiac_scorpio
             CAPRICORN -> R.drawable.zodiac_capricorn
             AQUARIUS -> R.drawable.zodiac_aquarius
             SAGITTARIUS -> R.drawable.zodiac_sagittarius
             PISCES -> R.drawable.zodiac_pisces

         }
    }

    fun zodiacLocalisedName(sign: ZodiacSign): String {

        return when(sign){
            ARIES -> "Овен"
            TAURUS -> "Бык"
            CANCER -> "Рак"
            GEMINI -> "Близнецы"
            LEO -> "Лев"
            VIRGO -> "Дева"
            LIBRA -> "Весы"
            SCORPIO -> "Скорпион"
            CAPRICORN -> "Козерог"
            AQUARIUS -> "Водолей"
            SAGITTARIUS -> "Стрелец"
            PISCES -> "Рыбы"

        }


    }
}
