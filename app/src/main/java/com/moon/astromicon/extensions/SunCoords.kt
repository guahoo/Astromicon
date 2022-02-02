package com.moon.astromicon.extensions

class SunCoords(val dec: Double, val ra: Double) {

    operator fun component1(): Double {
        return dec
    }

    operator fun component2(): Double {
        return ra
    }

    fun copy(dec: Double, ra: Double): SunCoords {
        return SunCoords(dec, ra)
    }

    override fun toString(): String {
        return "SunCoords(dec=" + dec + ", ra=" + ra + ")"
    }

    override fun hashCode(): Int {
        return java.lang.Double.hashCode(dec) * 31 + java.lang.Double.hashCode(ra)
    }

    override fun equals(var1: Any?): Boolean {
        return if (this !== var1) {
            if (var1 is SunCoords) {
                val var2 = var1
                if (java.lang.Double.compare(dec, var2.dec) == 0 && java.lang.Double.compare(
                        ra,
                        var2.ra
                    ) == 0
                ) {
                    return true
                }
            }
            false
        } else {
            true
        }
    }

    companion object {
        // $FF: synthetic method
        fun `copy$default`(
            var0: SunCoords,
            var1: Double,
            var3: Double,
            var5: Int,
            var6: Any?
        ): SunCoords {
            var var1 = var1
            var var3 = var3
            if (var5 and 1 != 0) {
                var1 = var0.dec
            }
            if (var5 and 2 != 0) {
                var3 = var0.ra
            }
            return var0.copy(var1, var3)
        }
    }
}