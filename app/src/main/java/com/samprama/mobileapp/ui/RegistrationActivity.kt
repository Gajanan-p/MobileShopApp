package com.samprama.mobileapp.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.samprama.mobileapp.MainActivity
import com.samprama.mobileapp.R
import com.samprama.mobileapp.utils.MyDBHelper
import com.samprama.mobileapp.webservice.RetrofitClient
import com.samprama.mobileapp.webservice.SendMessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mobileCompanyNameEditText: EditText
    private lateinit var modelNumberEditText: EditText
    private lateinit var customerNameEditText: EditText
    private lateinit var mobileNoEditText: EditText
    private lateinit var villageEditText: EditText
    private lateinit var problemEditText: EditText
    private lateinit var totalCostEditText: EditText
    private lateinit var depositAmount:EditText
    private lateinit var pendingAmount:EditText
    private lateinit var registrationButton: Button
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    private lateinit var dbHelper: MyDBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
        }

        try {
            dbHelper = MyDBHelper(this)
            val db = dbHelper.writableDatabase

            // Perform database operations here

            db.close()
        } catch (e: Exception) {
            // Handle exceptions here
            Log.e(TAG, "Error performing database operation: ${e.message}")
        }
        customerNameEditText = findViewById(R.id.edit_customer_reg_customer_name)

        mobileNoEditText = findViewById(R.id.edit_customer_reg_cust_mo_no)

        mobileCompanyNameEditText = findViewById(R.id.edit_customer_reg_comp_name)

        modelNumberEditText = findViewById(R.id.edit_customer_reg_model_no)

        problemEditText = findViewById(R.id.edit_customer_reg_problem)

        totalCostEditText = findViewById(R.id.edit_customer_reg_cost)

        depositAmount = findViewById(R.id.edit_customer_reg_deposit)

        pendingAmount = findViewById(R.id.edit_customer_reg_pending)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculatePendingAmount(totalCostEditText, depositAmount, pendingAmount)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        totalCostEditText.addTextChangedListener(textWatcher)
        depositAmount.addTextChangedListener(textWatcher)
        villageEditText = findViewById(R.id.edit_customer_reg_village)

        registrationButton = findViewById(R.id.button_reg_register)
        val today = dateFormat.format(Date())

        registrationButton.setOnClickListener {
            if(!TextUtils.isEmpty(mobileCompanyNameEditText.text.toString())
                && !TextUtils.isEmpty(modelNumberEditText.text.toString())
                && !TextUtils.isEmpty(customerNameEditText.text.toString())
                && !TextUtils.isEmpty(mobileNoEditText.text.toString())
                && !TextUtils.isEmpty(villageEditText.text.toString())
                && !TextUtils.isEmpty(problemEditText.text.toString())
                && !TextUtils.isEmpty(totalCostEditText.text.toString())) {
                dbHelper.insertData(
                    mobileCompanyNameEditText.text.toString(),
                    modelNumberEditText.text.toString(),
                    customerNameEditText.text.toString(),
                    mobileNoEditText.text.toString(),
                    villageEditText.text.toString(),
                    problemEditText.text.toString(),
                    totalCostEditText.text.toString(),
                    depositAmount.text.toString(),
                    pendingAmount.text.toString(),
                    today
                )
                val customerName=customerNameEditText.text.toString()
                val customerName1=HtmlCompat.fromHtml(customerName, HtmlCompat.FROM_HTML_MODE_COMPACT)


                val messagestr ="नमस्कार! "+customerName1+"\n " +
                        "तुमची नोंदणी यशस्वीरित्या पूर्ण झाली आहे.\n\n"+
                        "मोबाइल :-"+  mobileCompanyNameEditText.text.toString()+"\n" +
                        "मॉडेल क्रमांक:-"+modelNumberEditText.text.toString()+"\n"+
                        "समस्या:- "+problemEditText.text.toString()+"\n"+
                        "रक्कम:-"+totalCostEditText.text.toString()+"\n"+
                        "राजपूत मोबाईल ला भेट दिल्याबद्दल धन्यवाद"
                var phonestr ="91"+ mobileNoEditText.text.toString()

                val spannableString = SpannableString(messagestr)
                // Apply styles to specific parts of the message
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 10, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Apply bold style
                spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.blue)), 20, 26,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Apply color

                if (messagestr.isNotEmpty() && phonestr.isNotEmpty()) {

                    if (isWhatappInstalled()) {
                        val url=Uri.parse("https://api.whatsapp.com/send?phone=$phonestr&text=$spannableString")
                        val intent = Intent(Intent.ACTION_VIEW,url)

                        startActivity(intent)
                        problemEditText.setText("")
                        mobileNoEditText.setText("")
                        modelNumberEditText.setText("")
                        customerNameEditText.setText("")
                        villageEditText.setText("")
                        mobileCompanyNameEditText.setText("")
                        totalCostEditText.setText("")
                        depositAmount.setText("")
                        pendingAmount.setText("")
                    } else {
                        Toast.makeText(this@RegistrationActivity, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegistrationActivity, "Please fill in the phone number and message; they can't be empty", Toast.LENGTH_LONG).show()
                }

                Toast.makeText(this, "Record saved successfully !", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show()
            }
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_menu_action_collection -> {
                    //navController.navigate(R.id.navigation_home)
//                    showBottomDialog()
                    val intent = Intent(this@RegistrationActivity,CollectionActivity::class.java)
                    startActivity(intent)
                    // Toast.makeText(this,"Work in process!",Toast.LENGTH_LONG).show()
                    true
                }
                R.id.bottom_menu_action_registration -> {
                    //  navController.navigate(R.id.navigation_dashboard)
//                    val intent = Intent(this, RegistrationActivity::class.java)
//                    startActivity(intent)
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

    private fun isWhatappInstalled(): Boolean {
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            true
        }
        return true
    }
    private fun calculatePendingAmount(totalAmountEditText: EditText, depositAmountEditText: EditText, pendingAmountEditText: EditText) {
        val totalAmountText = totalAmountEditText.text.toString()
        val depositAmountText = depositAmountEditText.text.toString()

        val totalAmount = if (totalAmountText.isNotEmpty()) totalAmountText.toDouble() else 0.0
        val depositAmount = if (depositAmountText.isNotEmpty()) depositAmountText.toDouble() else 0.0

        val pendingAmount = totalAmount - depositAmount

        pendingAmountEditText.setText(pendingAmount.toString())
    }
}