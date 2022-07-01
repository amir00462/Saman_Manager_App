package ir.dunijet.saman_managment.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.JsonObject
import ir.dunijet.saman_managment.R
import ir.dunijet.saman_managment.me.LoginResponse
import ir.dunijet.saman_managment.me.TokenInMemory
import ir.dunijet.saman_managment.me.createApiService
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        btn_sign_in.setOnClickListener {

           signInUser()

        }

        btn_forget_password.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_sign_in_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun signInUser() {

        val email = et_email.text.toString().trim()
        val password = et_password.text.toString().trim()

        if (validateForm(email, password)) {

            val jsonObject = JsonObject().apply {
                addProperty("username", email)
                addProperty("password", password)
            }
            val apiService = createApiService()
            apiService.signIn(jsonObject).enqueue(object : Callback<LoginResponse> {

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                    val newData = response.body()!!
                    if(newData.success) {

                        val sharedPref = getSharedPreferences("main", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("token", newData.token).apply()
                        sharedPref.edit().putString("username", email).apply()
                        TokenInMemory.refreshToken(email , newData.token)

                        val intent = Intent(this@SigninActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@SigninActivity, newData.message, Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@SigninActivity, t.message, Toast.LENGTH_SHORT).show()
                }


            })

        }

    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email")
                false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }
    }

}