package ir.dunijet.saman_managment.model.repository.user

import android.content.SharedPreferences
import com.google.gson.JsonObject
import ir.dunijet.saman_managment.me.ApiService
import ir.dunijet.saman_managment.me.LoginResponse
import ir.dunijet.saman_managment.me.TokenInMemory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferences
) : UserRepository {

    override fun signUp(name: String, username: String, password: String): String {

        var result = "success"

        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("username", username)
            addProperty("password", password)
        }

        apiService.signUp(jsonObject).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val body = response.body()!!
                if (body.success) {
                    saveUserName(username)
                    result = body.message
                } else {
                    result = "failed"
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                result = "failed"
            }

        })

        return result
    }

    override fun signIn(username: String, password: String): String {

        var result = "success"

        val jsonObject = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
        }


        apiService.signIn(jsonObject).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                val body = response.body()!!
                if(body.success) {

                    TokenInMemory.refreshToken(username , body.token)
                    saveToken(body.token)
                    saveUserName(username)

                    result = body.message
                } else {
                    result = "failed"
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                result = "failed"
            }

        } )


        return result
    }

    override fun signOut() {
        TokenInMemory.refreshToken(null, null)
        sharedPref.edit().clear().apply()
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName(), getToken())
    }

    override fun saveToken(newToken: String) {
        sharedPref.edit().putString("token", newToken).apply()
    }

    override fun getToken(): String? {
        return sharedPref.getString("token", null)
    }

    override fun saveUserName(username: String) {
        sharedPref.edit().putString("username", username).apply()
    }

    override fun getUserName(): String? {
        return sharedPref.getString("username", null)
    }

}
