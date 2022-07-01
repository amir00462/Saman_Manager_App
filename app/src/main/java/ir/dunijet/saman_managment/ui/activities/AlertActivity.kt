package ir.dunijet.saman_managment.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ir.dunijet.saman_managment.R
import kotlinx.android.synthetic.main.activity_alert.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class AlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ir.dunijet.saman_managment.R.layout.activity_alert)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        val sharedPref = getSharedPreferences("mainFile", Context.MODE_PRIVATE)

        val useAlert = sharedPref.getBoolean("isAlertOn", false)
        switch_main.isSelected = useAlert

        when (sharedPref.getInt("alertSound", 0)) {

            1 -> {
                radio1.isSelected = true
            }

            2 -> {
                radio2.isSelected = true
            }

            3 -> {
                radio3.isSelected = true
            }

        }

        var tmpSelected = 0
        btn_update_main.setOnClickListener {

            sharedPref.edit().putBoolean("isAlertOn", switch_main.isSelected).apply()

            when {

                radio1.isSelected -> {
                    tmpSelected = 1
                }

                radio2.isSelected -> {
                    tmpSelected = 2
                }

                radio3.isSelected -> {
                    tmpSelected = 3
                }

            }

            sharedPref.edit().putInt("alertSound", tmpSelected).apply()

            Toast.makeText(this, "time updated...", Toast.LENGTH_SHORT).show()

        }

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_create_board_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = "Task Alert"
        }

        toolbar_create_board_activity.setNavigationOnClickListener { onBackPressed() }
    }

}