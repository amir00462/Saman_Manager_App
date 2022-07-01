package ir.dunijet.saman_managment.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ir.dunijet.saman_managment.R
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {
    private lateinit var sharedPref :SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()

        sharedPref = getSharedPreferences("main" , Context.MODE_PRIVATE)
        et_name.setText( sharedPref.getString("name" , "Sajad Ahmadi") )
        et_email.setText( sharedPref.getString("username" , "Sajad@gmail.com") )

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btn_update.setOnClickListener {
            Toast.makeText(this, "follow @dunijet in instagram to buy a coffee", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

}