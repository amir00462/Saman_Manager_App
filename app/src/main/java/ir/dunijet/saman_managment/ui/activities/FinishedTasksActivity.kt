package ir.dunijet.saman_managment.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ir.dunijet.saman_managment.R
import ir.dunijet.saman_managment.adapters.BoardItemsAdapter
import ir.dunijet.saman_managment.me.MyApiService
import ir.dunijet.saman_managment.me.MyTask
import kotlinx.android.synthetic.main.activity_finished_tasks.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedTasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished_tasks)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

    }

    override fun onResume() {
        super.onResume()
        loadDataFromServer()
    }

    private fun loadDataFromServer() {

        val api = MyApiService.apiService.getAllTasks()
        api.enqueue(object : Callback<MyTask> {

            override fun onResponse(call: Call<MyTask>, response: Response<MyTask>) {

                val data = response.body()!!
                if (data.sucess) {

                    recycler_finished.layoutManager = LinearLayoutManager(this@FinishedTasksActivity)
                    recycler_finished.setHasFixedSize(true)

                    val newList = data.tasks.filter { it.isDone == true }
                    val adapter = BoardItemsAdapter(this@FinishedTasksActivity, ArrayList(newList))
                    recycler_finished.adapter = adapter

                } else {
                    Toast.makeText(this@FinishedTasksActivity, "error in getting tasks...", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<MyTask>, t: Throwable) {
                Log.v("testApp", t.message ?: "null")
                Toast.makeText(this@FinishedTasksActivity, "error in getting tasks...", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_create_board_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = "finished tasks"
        }

        toolbar_create_board_activity.setNavigationOnClickListener { onBackPressed() }
    }

}