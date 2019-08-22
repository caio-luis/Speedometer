package e.caioluis.testevelocimetromvp.ui

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import e.caioluis.testevelocimetromvp.R
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private lateinit var context: Context
    private lateinit var mPresenter: MainActivityContract.Presenter
    private lateinit var lm: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVars()
        initActions()
    }

    private fun initVars() {

        context = this@MainActivity

        lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mPresenter = MainActivityPresenter(
            this,
            context,
            lm
        )
    }

    private fun initActions() {

        main_tbtn_start_stop.setOnCheckedChangeListener { _, isChecked ->

            when {
                !isChecked -> {

                    mPresenter.stopSpeedometer()

                    return@setOnCheckedChangeListener
                }
                !mPresenter.hasGPSPermission() -> {

                    mPresenter.requestGPSPermission()

                    mPresenter.setIdleState()

                    return@setOnCheckedChangeListener
                }
                !mPresenter.isLocationEnabled() -> {

                    mPresenter.requestProvider()

                    mPresenter.setIdleState()

                    return@setOnCheckedChangeListener
                }
                else -> mPresenter.startSpeedometer()
            }
        }
    }

    override fun showToastMessage(resource: Int) {

        Toast.makeText(
            context,
            resource,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showSpeedometerValue(value: String) {

        main_tv_speedometer.text = value
    }

    override fun toggleButton() {

        main_tbtn_start_stop.isChecked = !main_tbtn_start_stop.isChecked
    }

    override fun onStop() {

        if (main_tbtn_start_stop.isChecked)
            mPresenter.stopSpeedometer()
        super.onStop()
    }

    override fun onResume() {
        if (main_tbtn_start_stop.isChecked)
            mPresenter.setIdleState()
        super.onResume()
    }
}



