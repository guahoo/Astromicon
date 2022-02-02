package com.moon.astromicon.extensions

class MoonCords(val ra: Double, val dec: Double, val dist: Double) {

    operator fun component1(): Double {
        return ra
    }

    operator fun component2(): Double {
        return dec
    }

    operator fun component3(): Double {
        return dist
    }

    fun copy(ra: Double, dec: Double, dist: Double): MoonCords {
        return MoonCords(ra, dec, dist)
    }

    override fun toString(): String {
        return "MoonCords(ra=" + ra + ", dec=" + dec + ", dist=" + dist + ")"
    }

    override fun hashCode(): Int {
        return (java.lang.Double.hashCode(ra) * 31 + java.lang.Double.hashCode(dec)) * 31 + java.lang.Double.hashCode(
            dist
        )
    }

    override fun equals(var1: Any?): Boolean {
        return if (this !== var1) {
            if (var1 is MoonCords) {
                val var2 = var1
                if (java.lang.Double.compare(ra, var2.ra) == 0 && java.lang.Double.compare(
                        dec,
                        var2.dec
                    ) == 0 && java.lang.Double.compare(
                        dist, var2.dist
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
            var0: MoonCords,
            var1: Double,
            var3: Double,
            var5: Double,
            var7: Int,
            var8: Any?
        ): MoonCords {
            var var1 = var1
            var var3 = var3
            var var5 = var5
            if (var7 and 1 != 0) {
                var1 = var0.ra
            }
            if (var7 and 2 != 0) {
                var3 = var0.dec
            }
            if (var7 and 4 != 0) {
                var5 = var0.dist
            }
            return var0.copy(var1, var3, var5)
        }
    }
}
