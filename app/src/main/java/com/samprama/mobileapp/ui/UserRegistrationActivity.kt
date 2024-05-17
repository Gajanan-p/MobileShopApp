package com.samprama.mobileapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.textfield.TextInputEditText
import com.samprama.mobileapp.MainActivity
import com.samprama.mobileapp.R
import com.samprama.mobileapp.utils.MyDBHelper
import java.text.SimpleDateFormat
import java.util.*

class UserRegistrationActivity : AppCompatActivity() {
    private lateinit var dbHelper: MyDBHelper
    private lateinit var editTextUserName: TextInputEditText
    private lateinit var editTextMobileNo: TextInputEditText
    private lateinit var editTextMail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextConformPass: TextInputEditText
    private lateinit var buttonSubmit: AppCompatButton
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutUserData: LinearLayoutCompat
    private val mHandler = Handler()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
        progressBar = findViewById(R.id.progress_user_reg)
        layoutUserData = findViewById(R.id.layout_user_reg_data)
        dbHelper = MyDBHelper(this)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.blue)
            window.setTitle("Login")
        }

        editTextUserName=findViewById(R.id.edit_user_reg_user_name)
        editTextMobileNo=findViewById(R.id.edit_user_reg_mobile_no)
        editTextMail=findViewById(R.id.edit_user_reg_mail)
        editTextPassword=findViewById(R.id.edit_user_reg_password)
        editTextConformPass=findViewById(R.id.edit_user_reg_conform_pass)
        buttonSubmit = findViewById(R.id.button_user_reg_submit)
        val today = dateFormat.format(Date())
        buttonSubmit.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(editTextUserName.text.toString())
                &&!TextUtils.isEmpty(editTextMobileNo.text.toString())
                &&!TextUtils.isEmpty(editTextMail.text.toString())
                &&!TextUtils.isEmpty(editTextPassword.text.toString())
                &&!TextUtils.isEmpty(editTextConformPass.text.toString())) {
                val valuesEqual = areEditTextValuesEqual(editTextPassword, editTextConformPass)
                if (valuesEqual) {
                    // Values are equal
                    dbHelper.insertUserData(
                        editTextUserName.text.toString(), editTextMobileNo.text.toString(),
                        editTextMail.text.toString(), editTextConformPass.text.toString(),today
                    )

                    Toast.makeText(
                        this@UserRegistrationActivity,
                        "Your data saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    mHandler.postDelayed(
                        Runnable {
                            progressBar.visibility=View.VISIBLE
                            layoutUserData.visibility = View.GONE
                            val intent = Intent(this@UserRegistrationActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, //Here after the comma you specify the amount of time you want the screen to be delayed. 5000 is for 5 seconds.
                        2000
                    )

                } else {
                    // Values are not equal
                    Toast.makeText(this, "Password are not same!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Fill all fields!",Toast.LENGTH_SHORT).show()
            }

        })


    }
    private fun areEditTextValuesEqual(editText1: TextInputEditText, editText2: TextInputEditText): Boolean {
        // Get the text entered in the EditText fields
        val text1 = editText1.text.toString()
        val text2 = editText2.text.toString()

        // Compare the text values
        return text1 == text2
    }



}