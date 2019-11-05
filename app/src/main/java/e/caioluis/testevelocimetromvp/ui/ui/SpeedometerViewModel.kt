package e.caioluis.testevelocimetromvp.ui.ui

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpeedometerViewModel : ViewModel() {

    val speedLiveData = MutableLiveData<Int>()

    val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location?) {

            speedLiveData.value = convertMetersToKm(location!!.speed)
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }
    }

    private fun convertMetersToKm(speed: Float): Int {

        val speedAux = speed * 3.6
        return speedAux.toInt()
    }
}