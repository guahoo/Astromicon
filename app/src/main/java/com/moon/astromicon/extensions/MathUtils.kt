package com.moon.astromicon.extensions

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import kotlin.jvm.internal.Intrinsics

class MathUtils private constructor() {
    fun toJulian(date: LocalDateTime): Double {
        Intrinsics.checkParameterIsNotNull(date, "date")
        return date.toInstant(ZoneOffset.UTC).toEpochMilli()
            .toDouble() / 8.64E7 - 0.5 + 2440588.toDouble()
    }

    fun fromJulian(julian: Double): LocalDateTime {
        val var10000 = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(
                ((julian + 0.5 - 2440588.toDouble()) * 8.64E7).toLong()
            ), ZoneId.systemDefault()
        )
        Intrinsics.checkExpressionValueIsNotNull(
            var10000,
            "LocalDateTime.ofInstant(…, ZoneId.systemDefault())"
        )
        return var10000
    }

    fun toDays(date: LocalDateTime): Double {
        Intrinsics.checkParameterIsNotNull(date, "date")
        return toJulian(date) - 2451545.toDouble()
    }

    fun rightAscension(l: Double, b: Double): Double {
        var var5 = false
        val var10000 = Math.sin(l)
        var var16 = 0.40909994067971484
        var var10 = var10000
        var var7 = false
        var var12 = Math.cos(var16)
        var10 *= var12
        var5 = false
        var12 = Math.tan(b)
        var16 = 0.40909994067971484
        var7 = false
        val var14 = Math.sin(var16)
        var16 = var10 - var12 * var14
        var7 = false
        val var17 = Math.cos(l)
        val var9 = false
        return Math.atan2(var16, var17)
    }

    fun declination(l: Double, b: Double): Double {
        var var5 = false
        val var10000 = Math.sin(b)
        var var14 = 0.40909994067971484
        var var8 = var10000
        var var7 = false
        var var10 = Math.cos(var14)
        var8 *= var10
        var5 = false
        var10 = Math.cos(b)
        var14 = 0.40909994067971484
        var7 = false
        var var12 = Math.sin(var14)
        var10 *= var12
        var5 = false
        var12 = Math.sin(l)
        var14 = var8 + var10 * var12
        var7 = false
        return Math.asin(var14)
    }

    fun azimuth(H: Double, phi: Double, dec: Double): Double {
        val var7 = false
        val var19 = Math.sin(H)
        var var9 = false
        var var12 = Math.cos(H)
        var9 = false
        var var14 = Math.sin(phi)
        var12 *= var14
        var9 = false
        var14 = Math.tan(dec)
        var9 = false
        val var16 = Math.cos(phi)
        val var18 = var12 - var14 * var16
        val var11 = false
        return Math.atan2(var19, var18)
    }

    fun altitude(H: Double, phi: Double, dec: Double): Double {
        var var7 = false
        var var10 = Math.sin(phi)
        var7 = false
        var var12 = Math.sin(dec)
        var10 *= var12
        var7 = false
        var12 = Math.cos(phi)
        var7 = false
        var var14 = Math.cos(dec)
        var12 *= var14
        var7 = false
        var14 = Math.cos(H)
        val var16 = var10 + var12 * var14
        val var9 = false
        return Math.asin(var16)
    }

    fun siderealTime(d: Double, lw: Double): Double {
        return 0.017453292519943295 * (280.16 + 360.9856235 * d) - lw
    }

    fun astroRefraction(h: Double): Double {
        val hChecked = if (h < 0.toDouble()) h else h
        val var5 = hChecked + 0.00312536 / (hChecked + 0.08901179)
        val var8 = 2.967E-4
        val var7 = false
        val var10 = Math.tan(var5)
        return var8 / var10
    }

    fun julianCycle(d: Double, lw: Double): Double {
        val var5 = d - 9.0E-4 - lw / 6.283185307179586
        val var7 = false
        return Math.rint(var5)
    }

    fun approxTransit(Ht: Double, lw: Double, n: Double): Double {
        return 9.0E-4 + (Ht + lw) / 6.283185307179586 + n
    }

    fun solarTransitJ(ds: Double, M: Double, L: Double): Double {
        var var10000 = 2451545.toDouble() + ds
        var var12 = 0.0053
        var var10 = var10000
        val var7 = false
        var var14 = Math.sin(M)
        var10000 = var10 + var12 * var14
        val var16 = 2.toDouble() * L
        var12 = 0.0069
        var10 = var10000
        val var9 = false
        var14 = Math.sin(var16)
        return var10 - var12 * var14
    }

    fun hourAngle(h: Double, phi: Double, d: Double): Double {
        var var7 = false
        var var10 = Math.sin(h)
        var7 = false
        var var12 = Math.sin(phi)
        var7 = false
        var var14 = Math.sin(d)
        var10 -= var12 * var14
        var7 = false
        var12 = Math.cos(phi)
        var7 = false
        var14 = Math.cos(d)
        val var16 = var10 / (var12 * var14)
        val var9 = false
        return Math.cos(var16)
    }

    fun observerAngle(height: Double): Double {
        val var4 = -2.076
        val var3 = false
        val var6 = Math.sqrt(height)
        return var4 * var6 / 60.toDouble()
    }

    fun getSetJ(
        h: Double,
        lw: Double,
        phi: Double,
        dec: Double,
        n: Double,
        M: Double,
        L: Double
    ): Double {
        val w = hourAngle(h, phi, dec)
        val a = approxTransit(w, lw, n)
        return solarTransitJ(a, M, L)
    }

    fun solarMeanAnomaly(d: Double): Double {
        return 0.017453292519943295 * (357.5291 + 0.98560028 * d)
    }

    fun eclipticLongitude(M: Double): Double {
        var var10 = 1.9148
        val var8 = 0.017453292519943295
        val var5 = false
        var var12 = Math.sin(M)
        var var10001 = var10 * var12
        var P = 2.toDouble() * M
        var12 = 0.02
        var10 = var10001
        var var7 = false
        var var14 = Math.sin(P)
        var10001 = var10 + var12 * var14
        P = 3.toDouble() * M
        var12 = 3.0E-4
        var10 = var10001
        var7 = false
        var14 = Math.sin(P)
        val C = var8 * (var10 + var12 * var14)
        P = 1.796593062783907
        return M + C + P + 3.141592653589793
    }

    fun getSunCoords(d: Double): SunCoords {
        val M = solarMeanAnomaly(d)
        val L = eclipticLongitude(M)
        return SunCoords(declination(L, 0.0), rightAscension(L, 0.0))
    }

    fun normalize(value: Double): Double {
        val var5 = false
        val var8 = Math.floor(value)
        var v = value - var8
        if (v < 0.toDouble()) {
            v += 1.toDouble()
        }
        return v
    }

    fun getMoonCords(d: Double): MoonCords {
        val L = 0.017453292519943295 * (218.316 + 13.176396 * d)
        val M = 0.017453292519943295 * (134.963 + 13.064993 * d)
        val F = 0.017453292519943295 * (93.272 + 13.22935 * d)
        var var18 = 0.10976375665792339
        val var11 = false
        var var20 = Math.sin(M)
        val l = L + var18 * var20
        var var16 = 0.08950048404226922
        val var13 = false
        var18 = Math.sin(F)
        val b = var16 * var18
        val var10000 = 385001.toDouble()
        var18 = 20905.toDouble()
        var16 = var10000
        val var15 = false
        var20 = Math.cos(M)
        val dt = var16 - var18 * var20
        return MoonCords(rightAscension(l, b), declination(l, b), dt)
    }

    fun getSolarNoonAndNadir(
        latitude: Double,
        longitude: Double,
        date: LocalDateTime,
        height: Double
    ): Pair<*, *> {
        Intrinsics.checkParameterIsNotNull(date, "date")
        val lw = 0.017453292519943295 * -longitude
        val d = toDays(date)
        val n = julianCycle(d, lw)
        val ds = approxTransit(0.0, lw, n)
        val M = solarMeanAnomaly(ds)
        val L = eclipticLongitude(M)
        val Jnoon = solarTransitJ(ds, M, L)
        val solarNoon = fromJulian(Jnoon)
        val nadir = fromJulian(Jnoon - 0.5)
        return Pair<Any?, Any?>(solarNoon, nadir)
    }

    fun getTimeAndEndingByValue(
        latitude: Double,
        longitude: Double,
        date: LocalDateTime,
        height: Double,
        angle: Float
    ): Pair<*, *> {
        Intrinsics.checkParameterIsNotNull(date, "date")
        val lw = 0.017453292519943295 * -longitude
        val phi = 0.017453292519943295 * latitude
        val dh = observerAngle(height)
        val d = toDays(date)
        val n = julianCycle(d, lw)
        val ds = approxTransit(0.0, lw, n)
        val M = solarMeanAnomaly(ds)
        val L = eclipticLongitude(M)
        val dec = declination(L, 0.0)
        val Jnoon = solarTransitJ(ds, M, L)
        val Jset = getSetJ((angle.toDouble() + dh) * 0.017453292519943295, lw, phi, dec, n, M, L)
        val Jrise = Jnoon - (Jset - Jnoon)
        return Pair<Any?, Any?>(fromJulian(Jset), fromJulian(Jrise))
    }

    fun hoursLater(date: LocalDateTime, hoursLater: Int): LocalDateTime {
        Intrinsics.checkParameterIsNotNull(date, "date")
        val var10000 = date.plusMinutes((hoursLater * 60).toLong())
        Intrinsics.checkExpressionValueIsNotNull(
            var10000,
            "date.plusMinutes((hoursLater * 60).toLong())"
        )
        return var10000
    }

    fun closestValue(value: Float, values: FloatArray): Float {
        Intrinsics.checkParameterIsNotNull(values, "values")
        var min = Int.MAX_VALUE.toFloat()
        var closest = value

        val var8 = values.size
        for (var9 in 0 until var8) {
            val element = values[var9]

            val diff = Math.abs(element - value)
            if (diff < min) {
                min = diff
                closest = element
            }
        }
        return closest
    }

    companion object {
        var INSTANCE: MathUtils? = null

        init {
            val var0 = MathUtils()
            INSTANCE = var0
        }
    }
}