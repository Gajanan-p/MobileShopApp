package com.samprama.mobileapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.samprama.mobileapp.R
import com.samprama.mobileapp.adapters.SearchListItemAdapter
import com.samprama.mobileapp.adapters.TransactionsAdapter
import com.samprama.mobileapp.dialog.ActionButtonClickListener
import com.samprama.mobileapp.utils.MyDBHelper
import com.samprama.mobileapp.viewmodel.RegistrationModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CollectionActivity : AppCompatActivity(), ActionButtonClickListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textNoOfTransaction:AppCompatTextView
    private lateinit var textTotalCollection:AppCompatTextView
    private lateinit var textDepositAmount:AppCompatTextView
    private lateinit var textPendingAmount:AppCompatTextView
    private lateinit var recyclerViewList: RecyclerView
    private lateinit var dbHelper: MyDBHelper
    private lateinit var registrationAdapter: TransactionsAdapter
    private lateinit var tempData:ArrayList<RegistrationModel>
    private lateinit var itemList: MutableList<RegistrationModel>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var time=null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        dbHelper  = MyDBHelper(this)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
        }
        tempData=ArrayList()
        textNoOfTransaction = findViewById(R.id.text_no_of_transaction)
        textTotalCollection =findViewById(R.id.text_total_collection)
        textDepositAmount = findViewById(R.id.text_collection_deposit)
        textPendingAmount = findViewById(R.id.text_collection_pending)
        recyclerViewList = findViewById(R.id.list_total_collection)

        sharedPreferences = getSharedPreferences("DailyCollections", Context.MODE_PRIVATE)
        val currentDate = dateFormat.format(Date())
        val data = dbHelper.readData()
        itemList=dbHelper.readData()
        var totalAmount = ArrayList<Double>()
        var depositAmount = ArrayList<Double>()
        var pendingAmount = ArrayList<Double>()

//        val dateTimeString = "16-5-2024 05:56:37"
//        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd-Myyyy hh:mm:ss"))
//
//        val date = dateTime.toLocalDate()
////        val time = dateTime.toLocalTime()
//
//        println("Date: $date")
//        println("Time: $time")

        for (item in data){
//            val dateTime = LocalDateTime.parse(item.date, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))
//
//            val date = dateTime.toLocalDate()
//            val times = dateTime.toLocalTime()
         //   time= times as Nothing?
            if (item.date==currentDate){
                tempData.add(item)
                totalAmount.add(item.cost.toDouble())
                depositAmount.add(item.deposit.toDouble())
                pendingAmount.add(item.pending.toDouble())
            }
        }
        val totalCollection = calculateTotalWithLoop(depositAmount)
        textTotalCollection.text = "\u20B9"+totalCollection.toString()
        textNoOfTransaction.text = tempData.size.toString() +"  Payments"
        System.out.println(totalCollection)

        val totalDepositAmount=calculateDepositWithLoop(totalAmount)
        textDepositAmount.text =  "\u20B9"+totalDepositAmount

        val totalPendingAmount = calculatePendingWithLoop(pendingAmount)
        textPendingAmount.text = "\u20B9"+totalPendingAmount

        registrationAdapter = TransactionsAdapter(tempData,this)
        recyclerViewList.layoutManager = LinearLayoutManager(this)
        recyclerViewList.adapter = registrationAdapter
        registrationAdapter.notifyDataSetChanged()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_menu_action_collection -> {
                    //navController.navigate(R.id.navigation_home)
//                    showBottomDialog()
//                    val intent = Intent(this@CollectionActivity,CollectionActivity::class.java)
//                    startActivity(intent)
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
                    Toast.makeText(this,"Work in process!", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }


    fun calculateTotalWithLoop(prices: ArrayList<Double>): Double {
        var total = 0.0
        for (price in prices) {
            total += price
        }
        return total
    }
    fun calculateDepositWithLoop(prices: ArrayList<Double>): Double {
        var total = 0.0
        for (price in prices) {
            total += price
        }
        return total
    }
    fun calculatePendingWithLoop(prices: ArrayList<Double>): Double {
        var total = 0.0
        for (price in prices) {
            total += price
        }
        return total
    }
    override fun onItemClick(data: RegistrationModel, position: Int) {
        TODO("Not yet implemented")
    }



}