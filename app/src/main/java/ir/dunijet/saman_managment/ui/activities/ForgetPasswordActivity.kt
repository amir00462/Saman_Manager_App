package ir.dunijet.saman_managment.ui.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.JsonObject
import ir.dunijet.saman_managment.me.LoginResponse
import ir.dunijet.saman_managment.me.MyApiService
import kotlinx.android.synthetic.main.activity_forget_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ir.dunijet.saman_managment.R.layout.activity_forget_password)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btn_ok_password.setOnClickListener {
            Toast.makeText(this@ForgetPasswordActivity, "password reset to 1234", Toast.LENGTH_SHORT).show()
        }

    }

}