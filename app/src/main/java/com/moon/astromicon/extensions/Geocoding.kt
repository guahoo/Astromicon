package com.moon.astromicon.extensions

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class Geocoding {

    fun Context.geocoding(latitude: Double, longitude: Double): List<String> {
        val geocoder = Geocoder(this)

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            val stringBuilder = StringBuilder()
            if (addresses.size > 0) {
                val returnAddress = addresses[0]

                val localityString = returnAddress.locality
                val name = returnAddress.featureName
                val subLocality = returnAddress.subLocality
                val country = returnAddress.countryName
                val region_code = returnAddress.countryCode
                val zipcode = returnAddress.postalCode
                val state = returnAddress.adminArea

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return arrayListOf()

    }

    fun Context.getCoordsByCityName(cityName: String): MutableList<Address> {

        val addresses = mutableListOf<Address>()
        if (Geocoder.isPresent()) {
            try {
                val gc = Geocoder(this)

                addresses.addAll(
                    gc.getFromLocationName(
                        cityName,
                        5
                    )
                )// get the found Address Objects
                val ll: MutableList<LatLng> =
                    ArrayList(addresses.size) // A list to save the coordinates if they are available
                for (a in addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(LatLng(a.latitude, a.longitude))
                    }
                }
            } catch (e: IOException) {
                // handle the exception
            }
        }

        return addresses
    }
}