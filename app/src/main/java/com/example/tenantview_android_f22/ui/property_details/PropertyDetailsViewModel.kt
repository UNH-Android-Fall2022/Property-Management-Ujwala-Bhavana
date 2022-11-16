package com.example.tenantview_android_f22.ui.property_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PropertyDetailsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = null
    }
    val text: LiveData<String> = _text
}