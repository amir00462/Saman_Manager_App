package ir.dunijet.saman_managment.me

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LoginResponse(
    val message: String,
    val status: Int,
    val success: Boolean,
    val messages: List<eee.Message>,
    val token: String
) {
    data class Message(
        val name: String,
        val password: String,
        val username: String
    )
}

data class MyTask(
    val task: Task,
    val status: Int,
    val sucess: Boolean,
    val tasks: List<Task>
) {

    @Parcelize
    data class Task(
        val createdAt: String,
        val detail: String,

        @SerializedName("_id")
        val id: String,

        val isDone: Boolean,
        val name: String,
        val subject: String,
        val time: String,
        val updatedAt: String,
        val user: String,

        @SerializedName("__v")
        val v: Int
    ) :Parcelable

}

