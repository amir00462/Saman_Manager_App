package ir.dunijet.saman_managment.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import ir.dunijet.saman_managment.R
import ir.dunijet.saman_managment.me.TokenInMemory
import ir.dunijet.saman_managment.me.createApiService
import ir.dunijet.saman_managment.model.repository.user.UserRepository
import ir.dunijet.saman_managment.model.repository.user.UserRepositoryImpl
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon_bl.ttf")
        tv_app_name.typeface = typeFace

        sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE)

        sharedPreferences.edit().putString("usage", "5").apply()
        sharedPreferences.edit().putString("star", "5").apply()

        val token = sharedPreferences.getString("token", "null")
        val username = sharedPreferences.getString("username", "null")
        TokenInMemory.refreshToken(username, token)

        Handler().postDelayed({

            if (TokenInMemory.token != "null") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
            }

            finish()

        }, 2000)


    }
}