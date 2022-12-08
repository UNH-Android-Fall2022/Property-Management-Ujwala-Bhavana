package com.property.management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.property.management.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth

    private val TAG = "Testing"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignin.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnsignup.setOnClickListener{
            val name = binding.txtName.text.toString().trim()
            val email = binding.txtEmailid.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()
            val phoneNum = binding.txtphoneNum.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User creation successful")
                        val userid = auth.currentUser?.uid

                        //create Hashmap with user details
                        val user = hashMapOf(
                            "Name" to name,
                            "Email id" to email,
                            "Password" to password,
                            "Phone Number" to phoneNum
                        )

                        //add hashmap to collection
                        if (binding.rbtnowner.isChecked) {
                            if (userid != null) {
                                db.collection("Owners").document(userid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "owner successfully added to collection")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(
                                            TAG,
                                            "Error writing document to owners collection",
                                            exception
                                        )
                                    }
                            }
                        } /*else {
                            if (userid != null) {
                                db.collection("Tenants").document(userid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Tenant successfully added to collection")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(
                                            TAG,
                                            "Error writing document to Tenants collection",
                                            exception
                                        )
                                    }
                            }
                        }*/
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Log.d(TAG, "Error writing owner to collection")
                    }
                }
            binding.txtName.setText("")
            binding.txtEmailid.setText("")
            binding.txtPassword.setText("")
            binding.txtphoneNum.setText("")
        }

    }
}