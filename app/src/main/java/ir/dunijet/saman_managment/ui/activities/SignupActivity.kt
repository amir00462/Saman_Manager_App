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
import ir.dunijet.saman_managment.model.repository.user.UserRepository
import ir.dunijet.saman_managment.model.repository.user.UserRepositoryImpl
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun registerUser() {

        val name = et_name.text.toString().trim()
        val email = et_email.text.toString().trim()
        val password = et_password.text.toString().trim()

        if (validateForm(name, email, password)) {

            // sing up now
            val jsonObject = JsonObject().apply {
                addProperty("name", name)
                addProperty("username", email)
                addProperty("password", password)
            }

            val apiService = createApiService()
            apiService.signUp(jsonObject).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val data = response.body()!!
                    if (data.success) {

                        val jsonObjectSignIn = JsonObject().apply {
                            addProperty("username", email)
                            addProperty("password", password)
                        }
                        apiService.signIn(jsonObjectSignIn).enqueue( object :Callback<LoginResponse> {

                            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                                val newData = response.body()!!
                                if(newData.success) {

                                    val sharedPref = getSharedPreferences("main", Context.MODE_PRIVATE)
                                    sharedPref.edit().putString("token", newData.token).apply()
                                    sharedPref.edit().putString("username", email).apply()
                                    sharedPref.edit().putString("name", name).apply()
                                    TokenInMemory.refreshToken(email , newData.token)

                                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)

                                } else {

                                    Toast.makeText(this@SignupActivity, newData.message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@SignupActivity, t.message, Toast.LENGTH_SHORT).show()
                            }


                        } )

                    } else {
                        Toast.makeText(this@SignupActivity, data.message ?: "null", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

        }

    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }

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