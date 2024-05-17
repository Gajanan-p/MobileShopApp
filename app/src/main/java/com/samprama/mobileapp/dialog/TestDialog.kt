package com.samprama.mobileapp.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.samprama.mobileapp.R
import com.samprama.mobileapp.viewmodel.RegistrationModel

class TestDialog(context: Context,
                 var registrationData:RegistrationModel)
    : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.action_dialog_activity)

    }
}