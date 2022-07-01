package ir.dunijet.saman_managment.ui.activities

import android.annotation.SuppressLint
import android.app.TaskInfo
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.JsonObject
import ir.dunijet.saman_managment.R
import ir.dunijet.saman_managment.me.MyApiService
import ir.dunijet.saman_managment.me.MyTask
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateBoardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        val intentData = intent.getParcelableExtra<MyTask.Task>("key")
        if (intentData != null) {

            btn_create.visibility = View.INVISIBLE
            btn_update.visibility = View.VISIBLE

            et_board_subject.setText(intentData.subject)
            et_board_name.setText(intentData.name)
            et_board_detail.setText(intentData.detail)

        } else {
            btn_create.visibility = View.VISIBLE
            btn_update.visibility = View.INVISIBLE
        }



        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        btn_create.setOnClickListener {
            createBoard()
        }

        btn_update.setOnClickListener {
            updateBoard(intentData!!)
        }


    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_create_board_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.create_board_title)
        }

        toolbar_create_board_activity.setNavigationOnClickListener { onBackPressed() }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createBoard() {

        // get date
        //val df: DateFormat = SimpleDateFormat("EEE, MMM d, ''yy")
        //val date: String = df.format()

        val time = Calendar.getInstance().getTime().time
        val subject = et_board_subject.text.toString()
        val name = et_board_name.text.toString()
        val details = et_board_detail.text.toString()

        val jsonObject = JsonObject().apply {
            addProperty("subject", subject)
            addProperty("name", name)
            addProperty("detail", details)
            addProperty("time", time)
            addProperty("isDone", false)
        }

        val api = MyApiService.apiService.addNewTask(jsonObject)
        api.enqueue(object : Callback<MyTask> {

            override fun onResponse(call: Call<MyTask>, response: Response<MyTask>) {
                val data = response.body()!!
                if (data.sucess) {
                    Toast.makeText(this@CreateBoardActivity, "task created", Toast.LENGTH_SHORT)
                        .show()

                    val sharedPref = getSharedPreferences("main", Context.MODE_PRIVATE)
                    val myStars = sharedPref.getString("stars", "5")
                    sharedPref.edit().putString("stars", (myStars!!.toInt() + 5).toString()).apply()

                    finish()
                } else {
                    Toast.makeText(this@CreateBoardActivity, "task not created", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<MyTask>, t: Throwable) {
                Toast.makeText(this@CreateBoardActivity, "task not created", Toast.LENGTH_SHORT)
                    .show()
            }

        })


    }

    @SuppressLint("SimpleDateFormat")
    private fun updateBoard(model: MyTask.Task) {

        val jsonObject = JsonObject().apply {
            addProperty("subject", et_board_subject.text.toString())
            addProperty("name", et_board_name.text.toString())
            addProperty("detail", et_board_detail.text.toString())
            addProperty("time", model.time)
            addProperty("isDone", model.isDone)
        }
        val api = MyApiService.apiService.updateTask(model.id , jsonObject)
            .enqueue(object :Callback<MyTask> {

                override fun onResponse(call: Call<MyTask>, response: Response<MyTask>) {
                    val data = response.body()!!

                    if (data.sucess) {
                        Toast.makeText(this@CreateBoardActivity, "task updated", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        Toast.makeText(this@CreateBoardActivity, "task not updated", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MyTask>, t: Throwable) {
                    Toast.makeText(this@CreateBoardActivity, "task not updated", Toast.LENGTH_SHORT).show()
                }


            } )

    }

}