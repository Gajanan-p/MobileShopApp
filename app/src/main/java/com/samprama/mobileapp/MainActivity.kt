package com.samprama.mobileapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.samprama.mobileapp.adapters.SearchListItemAdapter
import com.samprama.mobileapp.dialog.ActionButtonClickListener
import com.samprama.mobileapp.dialog.ActionCustomDialog
import com.samprama.mobileapp.ui.CollectionActivity
import com.samprama.mobileapp.ui.LoginActivity
import com.samprama.mobileapp.ui.RegistrationActivity
import com.samprama.mobileapp.utils.MyDBHelper
import com.samprama.mobileapp.utils.PreferenceUtil
import com.samprama.mobileapp.viewmodel.LoginData
import com.samprama.mobileapp.viewmodel.RegistrationModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),ActionButtonClickListener {

    private lateinit var dbHelper: MyDBHelper
    private lateinit var sharedPreferences: SharedPreferences
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var textTotalCollection: AppCompatTextView
    private lateinit var navController: NavigationBarView
    private lateinit var registrationAdapter: SearchListItemAdapter
    private lateinit var customerListRecyclerView: RecyclerView
    private lateinit var loginData :LoginData
    private lateinit var registrationList: ArrayList<RegistrationModel>
    private lateinit var listData:ArrayList<RegistrationModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = MyDBHelper(this)
        // Set toolbar color
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
        }
        textTotalCollection  = findViewById(R.id.text_main_total_collection)

        sharedPreferences = getSharedPreferences("DailyCollections", Context.MODE_PRIVATE)


        // Create a SimpleDateFormat instance with the desired format pattern
        // Create a SimpleDateFormat instance with the desired format pattern
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val compareDate = "2024-06-05"
        // Create a Date object (or use any other source of date)
        // Create a Date object (or use any other source of date)
        val date = Date()

        // Format the date using the SimpleDateFormat


        // Format the date using the SimpleDateFormat
        val currentDateTimeString = sdf.format(date)

//        if (compareDate < currentDateTimeString) {
//            Toast.makeText(this@MainActivity, "Your app license expire !", Toast.LENGTH_SHORT)
//                .show()
//            PreferenceUtil.clearLoginDataPreferences(this)
//            val intent = Intent(this@MainActivity,LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }


        customerListRecyclerView = findViewById(R.id.recycler_view_customer_list)
        listData = dbHelper.readData()
        getCustomerDataONDatabase()
        getCustomerDataONDatabase()

        if (PreferenceUtil.getLoginDataPreferences(this)!=null) {
            loginData = PreferenceUtil.getLoginDataPreferences(this)!! // !! means non null asserted
            checkUserLogin(loginData)
        }else{
           val intent=Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Initialize the registrationList here
        registrationList = dbHelper.readData()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_menu_action_collection -> {
                    //navController.navigate(R.id.navigation_home)
//                    showBottomDialog()
                    val intent = Intent(this@MainActivity,CollectionActivity::class.java)
                    startActivity(intent)
                   // Toast.makeText(this,"Work in process!",Toast.LENGTH_LONG).show()
                    true
                }
                R.id.bottom_menu_action_registration -> {
                  //  navController.navigate(R.id.navigation_dashboard)
                    val intent = Intent(this, RegistrationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.bottom_menu_action_outword -> {
                   // navController.navigate(R.id.navigation_notifications)
                    Toast.makeText(this,"Work in process!",Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCustomerDataONDatabase()
    }

    // calling on create option menu
    // layout to inflate our menu file.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)

        // Get the search item from the menu
        val searchItem: MenuItem = menu?.findItem(R.id.action_search)!!

        // Get the SearchView from the search item
        val searchView: androidx.appcompat.widget.SearchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        // Set the query text listener for the search view
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query text submit event
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle query text change event
               registrationAdapter.filter.filter(newText)
                return true
            }
        })

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                // Handle search action
                getCustomerDataONDatabase()
                true
            }
            R.id.action_logout->{
                PreferenceUtil.clearLoginDataPreferences(this)
                val i = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(i)
                true
            }

            // Add more cases for other menu items if needed
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCustomerDataONDatabase(){
        val data = dbHelper.readData()
        registrationAdapter = SearchListItemAdapter(data,this)
        customerListRecyclerView.layoutManager = LinearLayoutManager(this)
        customerListRecyclerView.adapter = registrationAdapter
        registrationAdapter.notifyDataSetChanged()
    }
    override fun onItemClick(data: RegistrationModel, position: Int) {
        val dialogFragment = ActionCustomDialog(this,data)
        dialogFragment.show()
    }



    private fun checkUserLogin(loginData: LoginData?) {
        if (loginData == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }


    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val videoLayout = dialog.findViewById<LinearLayout>(R.id.layoutVideo)
        val textCollection = dialog.findViewById<TextView>(R.id.text_bottom_sheet_collection)
//        val shortsLayout = dialog.findViewById<LinearLayout>(R.id.layoutShorts)
//        val liveLayout = dialog.findViewById<LinearLayout>(R.id.layoutLive)
        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)
        videoLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this@MainActivity, "Upload a Video is clicked", Toast.LENGTH_SHORT)
                .show()
        }

     //textCollection.text = "Work in progress!"


//        shortsLayout.setOnClickListener {
//            dialog.dismiss()
//            Toast.makeText(this@MainActivity, "Create a short is Clicked", Toast.LENGTH_SHORT)
//                .show()
//        }
//        liveLayout.setOnClickListener {
//            dialog.dismiss()
//            Toast.makeText(this@MainActivity, "Go live is Clicked", Toast.LENGTH_SHORT).show()
//        }
        cancelButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }
    private fun loadTotal(date: String): Int {
        return sharedPreferences.getInt(date, 0)
    }

    private fun saveTotal(date: String, total: Int) {
        sharedPreferences.edit().putInt(date, total).apply()
    }
}