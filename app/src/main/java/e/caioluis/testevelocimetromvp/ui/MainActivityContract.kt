package e.caioluis.testevelocimetromvp.ui

interface MainActivityContract {

    interface View {

        fun toggleButton()
        fun showToastMessage(resource: Int)
        fun showSpeedometerValue(value: String)
    }

    interface Presenter {

        fun setIdleState()
        fun requestProvider()
        fun requestGPSPermission()
        fun hasGPSPermission(): Boolean
        fun isLocationEnabled(): Boolean
        fun startSpeedometer()
        fun stopSpeedometer()
    }
}