package e.caioluis.testevelocimetromvp.ui

interface MainActivityContract {

    interface View {
        fun showToastMessage(resource: Int)
    }

    interface Presenter {

        fun viewStopped()
        fun viewResumed()
        fun buttonToggled(isChecked: Boolean)
    }
}