package e.caioluis.testevelocimetromvp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import e.caioluis.testevelocimetromvp.R
import e.caioluis.testevelocimetromvp.ui.MainActivityContract.Presenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityPresenter(
    private val view: MainActivityContract.View,
    private val context: Context

) : Presenter {

    private val mainActivity = context as Activity
    private var locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun buttonToggled(isChecked: Boolean) {

        when {
            !isChecked -> {
                stopSpeedometer()
                return
            }
            !hasGPSPermission() -> {
                requestGPSPermission()
                setIdleState()
                return
            }
            !isLocationEnabled() -> {
                requestProvider()
                setIdleState()
                return
            }
            else -> startSpeedometer()
        }
    }

    private fun requestProvider() {
        view.showToastMessage(R.string.provider_disabled_message)
    }

    private fun stopSpeedometer() {

        locationManager.removeUpdates(locationListener)
        view.showToastMessage(R.string.idle_state_speedometer_message)
    }

    private fun isLocationEnabled(): Boolean {

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun requestGPSPermission() {

        val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(mainActivity, permission, 0)
    }

    @SuppressLint("MissingPermission")
    private fun startSpeedometer() {

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            locationListener
        )
        view.showToastMessage(R.string.gps_service_requisition_message)
    }

    private fun hasGPSPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setIdleState() {
        toggleButton()
        showSpeedometerValue(
            context
                .getString(R.string.speedometer_text_idle)
        )
    }

    private fun showSpeedometerValue(value: String) {
        mainActivity.main_tv_speedometer.text = value
    }

    override fun viewStopped() {
        if (mainActivity.main_tbtn_start_stop.isChecked)
            stopSpeedometer()
    }

    override fun viewResumed() {
        if (mainActivity.main_tbtn_start_stop.isChecked)
            setIdleState()
    }

    private fun toggleButton() = with(mainActivity) {

        main_tbtn_start_stop.isChecked = !main_tbtn_start_stop.isChecked
    }

    private val locationListener = object : LocationListener {

        val metersToKm = 3.6F

        override fun onLocationChanged(location: Location?) {

            val speed = location!!.speed * metersToKm
            showSpeedometerValue("%.2f".format(speed))
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

        override fun onProviderEnabled(p0: String?) {}

        override fun onProviderDisabled(p0: String?) {
            setIdleState()
        }
    }
}

