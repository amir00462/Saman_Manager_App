package ir.dunijet.saman_managment.me

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    fun signUp(@Body jsonObject: JsonObject): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/signin")
    fun signIn(@Body jsonObject: JsonObject): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @GET("task")
    fun getAllTasks(): Call<MyTask>

    @Headers("Content-Type: application/json")
    @POST("task")
    fun addNewTask(@Body jsonObject: JsonObject): Call<MyTask>

    @Headers("Content-Type: application/json")
    @PUT("task/{id}")
    fun updateTask(@Path("id") id: String , @Body jsonObject: JsonObject): Call<MyTask>

}

fun createApiService(): ApiService {

    val BASE_URL = "http://192.168.2.72:3000/samanManager/v1/"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {

            val oldRequest = it.request()

            val newRequest = oldRequest.newBuilder()
            if (TokenInMemory.token != null)
                if (TokenInMemory.token != "null")
                    newRequest.addHeader("authorization", "bearer " + TokenInMemory.token!!)

            newRequest.addHeader("Content-Type", "application/json")
            newRequest.method(oldRequest.method, oldRequest.body)

            return@addInterceptor it.proceed(newRequest.build())
        }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}