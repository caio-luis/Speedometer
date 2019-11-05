package e.caioluis.testevelocimetromvp.ui.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import e.caioluis.testevelocimetromvp.R
import kotlinx.android.synthetic.main.activity_speedo.*

open class SpeedometerActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var viewModel: SpeedometerViewModel
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedo)

        initVars()
        initActions()
    }

    private fun initVars() {

        context = this@SpeedometerActivity

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        viewModel = ViewModelProviders.of(this).get(SpeedometerViewModel::class.java)
    }

    private fun initActions() {

        viewModel.speedLiveData.observe(
            this,
            Observer { speed ->
                main_tv_speedometer.text = speed.toString()
            }
        )

        main_tbtn_start_stop.setOnCheckedChangeListener { _, isChecked ->

            when {

                !isChecked -> {
                    stopSpeedometer()
                    setIdleState()
                }

                isChecked -> {

                    if (!hasGpsPermission()) {
                        switchButton()
                        requestGpsPermission()
                        return@setOnCheckedChangeListener
                    }

                    if (!isLocationEnabled()) {
                        switchButton()
                        showToastMessage(getString(R.string.error_message_location_disabled))
                        return@setOnCheckedChangeListener
                    }

                    startSpeedometer()
                }
            }
        }
    }

    private fun switchButton() {
        main_tbtn_start_stop.isChecked = !main_tbtn_start_stop.isChecked
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    private fun startSpeedometer() {

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            viewModel.locationListener
        )
    }

    private fun stopSpeedometer() {
        locationManager.removeUpdates(viewModel.locationListener)
    }

    private fun isLocationEnabled(): Boolean {

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun requestGpsPermission() {

        val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(this, permission, 0)
    }

    private fun hasGpsPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setIdleState() {
        stopSpeedometer()
        main_tv_speedometer.text = getString(R.string.speedometer_text_idle)
    }

    override fun onStop() {

        if (main_tbtn_start_stop.isChecked) {
            setIdleState()
            switchButton()
        }
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty() || grantResults.first() == PackageManager.PERMISSION_DENIED) {
            return
        }
        switchButton()
        startSpeedometer()
    }
}