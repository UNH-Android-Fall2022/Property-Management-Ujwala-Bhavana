package com.property.management.tenant.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.MainActivity
import com.property.management.databinding.FragmentAccounttenantBinding
import java.security.MessageDigest

class AccountFragmentTenant : Fragment() {

    private var _binding: FragmentAccounttenantBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private var tenantID = ""
    private val auth = Firebase.auth
    lateinit var ownerId:String
    lateinit var name:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccounttenantBinding.inflate(inflater, container, false)
        val root: View = binding.root
        readTenantNameFromFirebase()

        val md = MessageDigest.getInstance("MD5")
        val uid = md.digest(auth.currentUser?.email.toString().trim().toByteArray(Charsets.UTF_8)).toHex()
        binding.textViewMyProfile.setOnClickListener{
            Log.d(TAG, "My Profile clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToMyProfile(tenantID)
            findNavController().navigate(action)
        }
        binding.textViewPropertyDetails.setOnClickListener {
            Log.d(TAG, "Property Details clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToPropertyDetailsFragment()
            findNavController().navigate(action)
        }
        binding.textViewNotification.setOnClickListener {
            Log.d(TAG, "Notifications clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToNotificationsFragment()
            findNavController().navigate(action)
        }
        binding.textViewChatWithOwner.setOnClickListener {
            Log.d(TAG, "Chat With Owner clicked")
            db.collection("Tenants").document(uid).get()
                .addOnCompleteListener { document ->
                    val m = document.result.getData()
                    ownerId = m?.get("ownerId").toString()
                    db.collection("Owners").document(ownerId).get()
                        .addOnCompleteListener { document ->
                            val m = document.result.getData()
                            name = m?.get("Name").toString()
                            val action =
                                AccountFragmentTenantDirections.actionNavigationAccountToChatWithOwnerFragment(ownerId,name)
                            findNavController().navigate(action)
                        }
                }
        }
        binding.textViewContactOwner.setOnClickListener {
            Log.d(TAG, "Contact Owner clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToContactOwnerFragment()
            findNavController().navigate(action)
        }
        binding.textViewSignOut.setOnClickListener{
            auth.signOut()
            val intent = Intent(activity,MainActivity::class.java)
            startActivity(intent)
        }


        return root
    }
    private fun readTenantNameFromFirebase(){
        db.collection("Tenant1")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d(TAG,"Tenant db from account fragment: ${document.id} => ${document.data}")
                    tenantID = document.id
                    val tenantName: TextView = binding.textViewTenant
                    tenantName.text = "Hello ".plus(document.data["firstName"].toString()).plus(" ").plus(document.data["lastName"].toString()).plus("!!")
                }
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}