package com.property.management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import com.property.management.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.property.management.owner.OwnerHomeActivity
import com.property.management.tenant.TenantHomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val TAG = "Testing"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignup.setOnClickListener{
            var intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        val rg : RadioGroup = binding.radiogrp
        //val checkedId = rg.checkedRadioButtonId

        binding.btnLogin.setOnClickListener{
            val email = binding.txtemail.text.toString().trim()
            val password = binding.txtpassword.text.toString().trim()
            Log.d(TAG,"Login button clicked")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        if(binding.rbtnowner.isChecked) {
                            //Log.d(TAG,"Owner radio button checked")
                            val intent = Intent(this, OwnerHomeActivity::class.java)
                            startActivity(intent)
                        }
                        else if(binding.rbtntenant.isChecked) {
                            val intent = Intent(this, TenantHomeActivity::class.java)
                            startActivity(intent)
                        }

                        else
                            Toast.makeText(applicationContext, "Select Owner or Tenant", Toast.LENGTH_SHORT).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }


        }

    }
}