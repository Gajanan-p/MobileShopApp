package com.samprama.mobileapp.dialog

import com.samprama.mobileapp.viewmodel.RegistrationModel

interface ActionButtonClickListener {
    fun onItemClick(data: RegistrationModel,position: Int)
}