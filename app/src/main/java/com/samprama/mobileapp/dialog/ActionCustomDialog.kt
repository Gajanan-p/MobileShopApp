package com.samprama.mobileapp.dialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.HtmlCompat
import com.google.android.material.textfield.TextInputEditText
import com.samprama.mobileapp.R
import com.samprama.mobileapp.utils.MyDBHelper
import com.samprama.mobileapp.viewmodel.RegistrationModel

class ActionCustomDialog (context: Context,
                          var registrationData:RegistrationModel)
    : Dialog(context) {

    private lateinit var dbHelper: MyDBHelper
    private lateinit var editTextMobileCompanyName: TextInputEditText
    private lateinit var editTextModelName: TextInputEditText
    private lateinit var editTextCustomerName: TextInputEditText
    private lateinit var editTextMobileNumber: TextInputEditText
    private lateinit var editTextVillage: TextInputEditText
    private lateinit var editTextProblem: TextInputEditText
    private lateinit var editTextAmount: TextInputEditText
    private lateinit var buttonCloseDialog: AppCompatImageView
    private lateinit var buttonDelete : AppCompatButton
    private lateinit var buttonUpdate : AppCompatButton
    private lateinit var date:String


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.action_dialog_activity)
        // Initialize dbHelper
        dbHelper = MyDBHelper(context)
        editTextMobileCompanyName = findViewById(R.id.dialog_edit_customer_reg_comp_name)
        editTextModelName = findViewById(R.id.dialog_edit_customer_reg_model_no)
        editTextCustomerName = findViewById(R.id.dialog_edit_customer_reg_customer_name)
        editTextMobileNumber = findViewById(R.id.dialog_edit_customer_reg_cust_mo_no)
        editTextVillage = findViewById(R.id.dialog_edit_customer_reg_village)
        editTextProblem = findViewById(R.id.dialog_edit_customer_reg_problem)
        editTextAmount = findViewById(R.id.dialog_edit_customer_reg_cost)
        buttonCloseDialog = findViewById(R.id.button_dialog_close)
        buttonCloseDialog.setOnClickListener(View.OnClickListener {
            dismiss()
        })
        buttonDelete = findViewById(R.id.dialog_button_delete)
        buttonDelete.setOnClickListener(View.OnClickListener {
            dbHelper.deleteData(registrationData.id)
            dismiss()
        })
        buttonUpdate = findViewById(R.id.dialog_button_edit)
        buttonUpdate.setOnClickListener(View.OnClickListener {
            dbHelper.updateData(registrationData.id,editTextMobileCompanyName.text.toString(),editTextModelName.text.toString(),editTextCustomerName.text.toString(),
            editTextMobileNumber.text.toString(),editTextVillage.text.toString(),editTextProblem.text.toString(),editTextAmount.text.toString(),date)

            val customerName=editTextCustomerName.text.toString()
            val customerName1= HtmlCompat.fromHtml(customerName, HtmlCompat.FROM_HTML_MODE_COMPACT)


            val messagestr ="नमस्कार! "+customerName1+"\n " +
                    "तुमची नोंदणी यशस्वीरित्या पूर्ण झाली आहे.\n\n"+
                    "मोबाइल :-"+  editTextMobileCompanyName.text.toString()+"\n" +
                    "मॉडेल क्रमांक:-"+editTextMobileNumber.text.toString()+"\n"+
                    "समस्या:- "+editTextProblem.text.toString()+"\n"+
                    "रक्कम:-"+editTextAmount.text.toString()+"\n"+
                    "राजपूत मोबाईल ला भेट दिल्याबद्दल धन्यवाद"
            var phonestr ="91"+ editTextMobileNumber.text.toString()
            val spannableString = SpannableString(messagestr)
            // Apply styles to specific parts of the message


            if (messagestr.isNotEmpty() && phonestr.isNotEmpty()) {

               if (isWhatappInstalled()) {
                    val url= Uri.parse("https://api.whatsapp.com/send?phone=$phonestr&text=$spannableString")
                    val intent = Intent(Intent.ACTION_VIEW,url)

                    context.startActivity(intent)

                } else {
                    Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill in the phone number and message; they can't be empty", Toast.LENGTH_LONG).show()
            }

            Toast.makeText(context, "Record saved successfully !", Toast.LENGTH_SHORT).show()
            dismiss()
        })
        getRegistrationData()

    }

    fun getRegistrationData(){
        editTextMobileCompanyName.setText(registrationData.mobileCompanyName)
        editTextModelName.setText(registrationData.modelNumber)
        editTextCustomerName.setText(registrationData.customerName)
        editTextMobileNumber.setText(registrationData.customerMobileNo)
        editTextVillage.setText(registrationData.villageName)
        editTextProblem.setText(registrationData.problem)
        editTextAmount.setText(registrationData.cost)
        date=registrationData.date
    }

    private fun isWhatappInstalled(): Boolean {
        try {
           context.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            true
        }
        return true
    }
}