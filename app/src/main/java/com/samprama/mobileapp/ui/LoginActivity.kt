package com.samprama.mobileapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.set
import com.samprama.mobileapp.MainActivity
import com.samprama.mobileapp.R
import com.samprama.mobileapp.utils.MyDBHelper
import com.samprama.mobileapp.utils.PreferenceUtil
import com.samprama.mobileapp.viewmodel.LoginData
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper:MyDBHelper
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var layout: LinearLayoutCompat
    private var buttonLogin: AppCompatButton? = null
    var loginData: LoginData? = null
    private val mHandler = Handler()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dbHelper=MyDBHelper(this)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
            window.setTitle("Login")
        }
        // Create a SimpleDateFormat instance with the desired format pattern

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val compareDate = "2024-06-05"
        // Create a Date object (or use any other source of date)

        val date = Date()

        // Format the date using the SimpleDateFormat

        val currentDateTimeString = sdf.format(date)
        System.out.println(currentDateTimeString)
        val textViewCreate= findViewById<TextView>(R.id.text_login_create)
        textViewCreate.text = "Create"
        textViewCreate.paintFlags = textViewCreate.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textViewCreate.setOnClickListener(View.OnClickListener {
            val intent=Intent(this@LoginActivity,UserRegistrationActivity::class.java)
            startActivity(intent)
        })

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        progressBar = findViewById(R.id.progress_login)
        layout=findViewById(R.id.layout_login_data)
        // Add a TextWatcher to monitor changes in the EditText

        buttonLogin = findViewById(R.id.buttonLogin)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
            window.setTitle("Login")
        }

        loginData = LoginData()
        buttonLogin?.setOnClickListener(View.OnClickListener {
//            if (compareDate <currentDateTimeString) {
//                Toast.makeText(this@LoginActivity, "Your app license expire !", Toast.LENGTH_LONG)
//                    .show()
//            }else{
                if (!TextUtils.isEmpty(editTextEmail.text.toString()) && (!TextUtils.isEmpty(editTextPassword.text.toString()))) {
                    getUserLoginData()
                }else{
                    Toast.makeText(this,"Fill all fields!",Toast.LENGTH_SHORT).show()
                }
          //  }

        })

    }

    private fun getUserLoginData(){
        val email = editTextEmail?.text.toString().replace("\\s".toRegex(), "")
        val password = editTextPassword?.text.toString().replace("\\s".toRegex(), "")
        val data =dbHelper.readUserData()
        if (data.size!=0){
            for (item in data) {
                if (item.mobileNo.equals(email)||item.mail.equals(email)&&item.password.equals(password)){
                    // Simulate successful login
                    loginData!!.setUserName(email)
                    loginData!!.setPassword(password)
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    //  loginData.setPassword("1234");
                    PreferenceUtil.setLoginDataPreferences(this@LoginActivity, loginData!!)
                    //                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                    //                    navController.navigate(R.id.nav_home);

                    mHandler.postDelayed(
                        Runnable {
                            progressBar.visibility=View.VISIBLE
                            layout.visibility = View.GONE
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                                 }, //Here after the comma you specify the amount of time you want the screen to be delayed. 5000 is for 5 seconds.
                        2000
                    )
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid username or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }else{
            Toast.makeText(
                this@LoginActivity,
                "No user data found please create new user!",
                Toast.LENGTH_SHORT
            ).show()
            editTextEmail.text.clear()
            editTextPassword.text.clear()
        }

    }


}