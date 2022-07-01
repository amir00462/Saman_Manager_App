package ir.dunijet.saman_managment.ui.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import ir.dunijet.saman_managment.adapters.BoardItemsAdapter
import ir.dunijet.saman_managment.R
import ir.dunijet.saman_managment.me.MyApiService
import ir.dunijet.saman_managment.me.MyTask
import ir.dunijet.saman_managment.model.User
import ir.dunijet.saman_managment.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    BoardItemsAdapter.TaskEvent {

    companion object {
        const val CREATE_BOARD_REQUEST_CODE = 12
    }

    lateinit var adapter: BoardItemsAdapter
    private lateinit var mainSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        nav_view.setNavigationItemSelectedListener(this)

        mainSharedPref = getSharedPreferences("main", Context.MODE_PRIVATE)

        fab_create_board.setOnClickListener {
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mainSharedPref.getString("username", "null"))
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }

    }

    override fun onResume() {
        super.onResume()

        loadDataFromServer()
        loadStars()

    }

    private fun loadStars() {

        val myStars = mainSharedPref.getString("stars", "5")
        tv_star.text = "$" + myStars

    }

    private fun loadDataFromServer() {

        val api = MyApiService.apiService.getAllTasks()
        api.enqueue(object : Callback<MyTask> {

            override fun onResponse(call: Call<MyTask>, response: Response<MyTask>) {

                val data = response.body()!!
                if (data.sucess) {


                    rv_boards_list.visibility = View.VISIBLE
                    tv_no_boards_available.visibility = View.INVISIBLE

                    rv_boards_list.layoutManager = LinearLayoutManager(this@MainActivity)
                    rv_boards_list.setHasFixedSize(true)

                    val newList = data.tasks.filter { it.isDone == false }
                    adapter = BoardItemsAdapter(this@MainActivity, ArrayList(newList), this@MainActivity
                    )
                    rv_boards_list.adapter = adapter

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "error in getting tasks...",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<MyTask>, t: Throwable) {
                Log.v("testApp", t.message ?: "null")
                Toast.makeText(this@MainActivity, "error in getting tasks...", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_main_activty)
        toolbar_main_activty.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolbar_main_activty.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_my_profile -> {

                val intent = Intent(this, MyProfileActivity::class.java)
                startActivity(intent)

            }

            R.id.nav_sign_out -> {

                mainSharedPref.edit().clear().apply()

                Toast.makeText(this, "user signed out successfully...", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.nav_finished_tasks -> {
                val intent = Intent(this, FinishedTasksActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.nav_task_alert -> {
                val intent = Intent(this, AlertActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.nav_app_usage -> {
                val layout = layoutInflater.inflate(R.layout.dialog_app_usage, null)
                val dialog = AlertDialog.Builder(this)
                dialog.setView(layout)
                dialog.show()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClicked(position: Int, model: MyTask.Task) {

        // go edit item

        val intent = Intent(this@MainActivity, CreateBoardActivity::class.java)
        intent.putExtra("key", model)
        startActivity(intent)

    }

    override fun onLongClicked(position: Int, model: MyTask.Task) {

        // go remove item

        val layout = layoutInflater.inflate(R.layout.dialog_remove_task, null)
        val dialog = AlertDialog.Builder(this).create()
        dialog.setView(layout)

        layout.findViewById<TextView>(R.id.btn_remove_item_yes).setOnClickListener {

            val jsonObject = JsonObject().apply {
                addProperty("subject", model.subject)
                addProperty("name", model.name)
                addProperty("detail", model.detail)
                addProperty("time", model.time)
                addProperty("isDone", true)
            }
            val api = MyApiService.apiService.updateTask(model.id, jsonObject)
                .enqueue(object : Callback<MyTask> {
                    override fun onResponse(call: Call<MyTask>, response: Response<MyTask>) {
                        Toast.makeText(this@MainActivity, "task is done now...", Toast.LENGTH_SHORT)
                            .show()
                        adapter.removeItem(model, position)
                    }

                    override fun onFailure(call: Call<MyTask>, t: Throwable) {
                        Toast.makeText(
                            this@MainActivity,
                            "can't edit this task...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })

            dialog.dismiss()
        }

        layout.findViewById<TextView>(R.id.btn_remove_item_no).setOnClickListener {

            dialog.dismiss()

        }

        dialog.show()
    }


}