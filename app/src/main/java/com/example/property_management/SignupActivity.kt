package com.example.property_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.property_management.databinding.ActivitySignupBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val db = Firebase.firestore
    private val TAG = "Testing"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignin.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnsignup.setOnClickListener{
            val name = binding.txtName
            val email = binding.txtEmailid
            val password = binding.txtPassword
            val phoneNum = binding.txtphoneNum
            val user = hashMapOf(
                "Name" to name.text.toString(),
                "Email id" to email.text.toString(),
                "Password" to password.text.toString(),
                "Phone Number" to phoneNum.text.toString()
            )
            if(binding.rbtnowner.isChecked)
            {
                db.collection("Owners")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG,"owner successfully added to collection with id ${documentReference.id}")
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG,"Error writing document to owners collection")
                    }
            }
            else
            {
                db.collection("Tenants")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG,"Tenant successfully added to collection with id ${documentReference.id}")
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG,"Error writing document to Tenants collection")
                    }
            }
            name.setText("")
            email.setText("")
            password.setText("")
            phoneNum.setText("")
        }

    }
}