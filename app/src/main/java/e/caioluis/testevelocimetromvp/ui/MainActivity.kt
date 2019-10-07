package e.caioluis.testevelocimetromvp.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import e.caioluis.testevelocimetromvp.R
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private lateinit var context: Context
    private lateinit var presenter: MainActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val context = this@MainActivity

        presenter = MainActivityPresenter(this, context)

        main_tbtn_start_stop.setOnCheckedChangeListener { _, isChecked ->

            presenter.buttonToggled(isChecked)
        }
    }

    override fun showToastMessage(resource: Int) {

        Toast.makeText(
            context,
            resource,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStop() {
        super.onStop()
        presenter.viewStopped()
    }

    override fun onResume() {
        super.onResume()
        presenter.viewResumed()
    }
}



