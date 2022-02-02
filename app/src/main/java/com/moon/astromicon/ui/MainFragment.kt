package com.moon.astromicon.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.moon.astromicon.R
import com.moon.astromicon.extensions.LocationHelper
import com.moon.astromicon.extensions.MoonPickerView
import com.moon.astromicon.extensions.formatPickedDateTime
import com.moon.astromicon.extensions.getMoonPhase
import com.moon.astromicon.models.Coordinates
import com.moon.astromicon.models.DayOfWeekForecast
import com.moon.astromicon.moondata.ComputeMoonPosition
import kotlinx.android.synthetic.main.lunar_fragment.*

import org.joda.time.DateTime
import org.joda.time.LocalDate

import org.shredzone.commons.suncalc.MoonIllumination
import java.time.ZonedDateTime
import android.content.DialogInterface

import android.content.Intent

import android.location.LocationManager
import android.provider.Settings
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import android.app.Activity
import android.os.Looper
import android.widget.Toast
import androidx.annotation.Nullable
import org.joda.time.LocalDateTime

class MainFragment : Fragment() {


    companion object {
        fun newInstance() = MainFragment()
    }
    private var REQUEST_CODE_CHECK_SETTINGS = 123

    private lateinit var viewModel: MainViewModel
    private lateinit var activityResultLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var location: Coordinates

    val currentDay = DateTime.now().dayOfMonth
    val currentMonth = DateTime.now().monthOfYear
    val currentYear = DateTime.now().yearOfEra
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    init {

        this.activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true

            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if(allAreGranted) {
              initialWheelPicker()
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.lunar_fragment, container, false)
    }

    //
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
       fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        /**
         * проверка разрешений на определение локации
         */
        val appPerms = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        activityResultLauncher.launch(appPerms)

  day_time_picker_view.setWheelListener(object : MoonPickerView.Listener {
            override fun didSelectData(year: Int, month: Int, day: Int) {
                day_time_picker_view.isCircular = true
                val pickedYear = year
                val pickedMonth = month + 1
                val pickedDay = day


                try {
                    val pickedMoon = ComputeMoonPosition().setMoon(
                       LocalDateTime(pickedYear, pickedMonth, pickedDay,12,0,0), Coordinates(
                            location.lat, location.lon
                        )
                    )

                 //   println("Фаза ${pickedMoon[pickedMoon.size.toLong()]?.moonPhase!!.closestPhase} ${pickedMoon[pickedMoon.size.toLong()]?.moonPhase!!.phase}")

                    val pickedZodiacSign = ComputeMoonPosition().getZodiacSign(
                        org.threeten.bp.LocalDate.of(
                            pickedYear,
                            pickedMonth,
                            pickedDay
                        )
                    )

                    iv_zodiac_sign.setImageResource(pickedZodiacSign.ordinal(pickedZodiacSign))



                    iv_moon_sign.setImageResource(getMoonPhase((pickedMoon.fraction)).icon)

                    Glide.with(requireContext()).load(R.drawable.black_moon).apply(RequestOptions.circleCropTransform()).into(black_moon)
                    tv_rise.text =
                        "Восход луны " + formatPickedDateTime(pickedMoon.moonRise)
                    tv_set.text =
                        "Заход луны " + formatPickedDateTime(pickedMoon.moonSet)
                    tv_phase.text =
                        getMoonPhase((pickedMoon.moonPhase!!.phase)).title

                    tv_moon_day.text =
                        pickedMoon.moonDay.toString() + " - й лунный день"


                    zodiac_title.text = "Символ дня: ${pickedZodiacSign.zodiacLocalisedName(pickedZodiacSign)}"


                    day_of_week_forecast.text = resources.getString(DayOfWeekForecast().getForecast(
                       java.time.LocalDate.of(pickedYear,
                           pickedMonth,
                           pickedDay).dayOfWeek
                    ))

                    ComputeMoonPosition().animateMoon(
                        MoonIllumination.compute().on(pickedYear,pickedMonth,pickedDay).execute(),
                        black_moon
                    )

                } catch (e: Exception) {
                    Log.d("DATEEXEPTION", e.message.toString())
                }
            }

        })

    }








    private fun initialWheelPicker() {

        /**
         * определение локации
         */

       if (statusCheck()) {
           LocationHelper(fusedLocationClient, requireContext()) {

               location = Coordinates(it.latitude, it.longitude)
               println("Вы находитесь тут ${location.lat} ${location.lon}")
               Handler().postDelayed({
                   day_time_picker_view.setDate(currentYear, currentMonth - 1, currentDay)
                   day_time_picker_view.isHapticFeedbackEnabled = false
                   loaderScreen.visibility = View.GONE
               }, 500)

           }.getLastKnownLocation()
       }
    }

    private fun statusCheck(): Boolean {
        val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }

        return true
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Включение определения местоположения необходимо для точного определения времени лунных событий \n" +
                "Включить определение местоположения?")
            .setCancelable(false)
            .setPositiveButton("Да"
            ) { dialog, id -> startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                REQUEST_CODE_CHECK_SETTINGS) }
            .setNegativeButton("Нет"
            ) { dialog, id -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

     override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {

         println("резалт $requestCode $resultCode ${Activity.RESULT_OK}")
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if (Activity.RESULT_OK == -1) {
                initialWheelPicker()

                //user clicked OK, you can startUpdatingLocation(...);
            } else {
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }
    }
}






