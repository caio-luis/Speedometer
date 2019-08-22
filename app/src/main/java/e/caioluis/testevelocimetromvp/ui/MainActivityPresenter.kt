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

class MainActivityPresenter(

    private val mView: MainActivityContract.View,
    private val context: Context,
    private var lm: LocationManager

) : Presenter {

    override fun requestProvider() {
        mView.showToastMessage(R.string.provider_disabled_message)
    }

    override fun stopSpeedometer() {

        lm.removeUpdates(locationListener)
        mView.showToastMessage(R.string.idle_state_speedometer_message)
    }

    override fun isLocationEnabled(): Boolean {

        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }

    override fun requestGPSPermission() {

        val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(context as Activity, permission, 0)
    }

    @SuppressLint("MissingPermission")
    override fun startSpeedometer() {

        lm.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            locationListener
        )
        mView.showToastMessage(R.string.gps_service_requisition_message)
    }

    override fun hasGPSPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun setIdleState() {
        mView.toggleButton()
        mView.showSpeedometerValue(context
                .getString(R.string.speedometer_text_idle))
    }

    private val locationListener = object : LocationListener {

        val metersToKm = 3.6F

        override fun onLocationChanged(location: Location?) {

            val speed = location!!.speed * metersToKm

            mView.showSpeedometerValue("%.2f".format(speed))
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {

            setIdleState()
        }
    }
}

