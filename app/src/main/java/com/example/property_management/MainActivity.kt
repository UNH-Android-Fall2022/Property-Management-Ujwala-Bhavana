package com.example.property_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.get
import com.example.property_management.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val TAG = "Testing"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignup.setOnClickListener{
            var intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        val rg : RadioGroup = binding.radiogrp
        //val checkedId = rg.checkedRadioButtonId

        binding.btnLogin.setOnClickListener{
            Log.d(TAG,"Login button clicked")
            if(binding.rbtnowner.isChecked) {
                //Log.d(TAG,"Owner radio button checked")
                val intent = Intent(this,PropertiesActivity::class.java)
                startActivity(intent)
            }

            else if(binding.rbtntenant.isChecked)
                Log.d(TAG,"Tenant radio button checked")
            else
                Toast.makeText(applicationContext, "Select Owner or Tenant", Toast.LENGTH_SHORT).show()
        }

    }
}