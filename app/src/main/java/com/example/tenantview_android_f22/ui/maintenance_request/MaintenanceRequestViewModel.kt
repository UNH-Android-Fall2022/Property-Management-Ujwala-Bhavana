package com.example.tenantview_android_f22.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MaintenanceRequestViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Maintenance Request Fragment"
    }
    val text: LiveData<String> = _text
}