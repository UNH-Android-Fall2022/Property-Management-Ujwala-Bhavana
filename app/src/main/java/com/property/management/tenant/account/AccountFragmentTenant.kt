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
    private var ownerID = ""
    private var propertyID = ""
    private var unitID = ""
    private var rentAmount = ""
    private val auth = Firebase.auth
    private lateinit var ownerUid:String
    private lateinit var name:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccounttenantBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val md = MessageDigest.getInstance("MD5")
        tenantID = md.digest(auth.currentUser!!.email!!.toString().trim().toByteArray(Charsets.UTF_8)).toHex()
        readTenantNameFromFirebase()
        binding.textViewMyProfile.setOnClickListener{
            Log.d(TAG, "My Profile clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToMyProfile(tenantID)
            findNavController().navigate(action)
        }
        binding.textViewPropertyDetails.setOnClickListener {
            Log.d(TAG, "Property Details clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToPropertyDetailsFragment(unitID,propertyID,ownerID)
            findNavController().navigate(action)
        }
        binding.textViewNotification.setOnClickListener {
            Log.d(TAG, "Notifications clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToNotificationsFragment(tenantID,rentAmount)
            findNavController().navigate(action)
        }
        binding.textViewChatWithOwner.setOnClickListener {
            Log.d(TAG, "Chat With Owner clicked")
            db.collection("Tenants").document(tenantID).get()
                .addOnCompleteListener { document ->
                    val m = document.result.getData()
                    ownerUid = m?.get("ownerId").toString()
                    db.collection("Owners").document(ownerUid).get()
                        .addOnCompleteListener { document ->
                            val m = document.result.getData()
                            name = m?.get("Name").toString()
                            val action =
                                AccountFragmentTenantDirections.actionNavigationAccountToChatWithOwnerFragment(ownerUid,name)
                            findNavController().navigate(action)
                        }
                }
        }
        binding.textViewContactOwner.setOnClickListener {
            Log.d(TAG, "Contact Owner clicked")
            val action =
                AccountFragmentTenantDirections.actionNavigationAccountToContactOwnerFragment(ownerID)
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
        db.collection("Tenants").document(tenantID)
            .get()
            .addOnSuccessListener { documents ->
                propertyID = documents.data?.get("propertyId").toString()
                unitID = documents.data?.get("unitId").toString()
                ownerID = documents.data?.get("ownerId").toString()
                rentAmount =  documents.data?.get("rent").toString()
                val tenantName: TextView = binding.textViewTenant
                tenantName.text = "Hello ".plus(documents.data?.get("firstName")).plus(" ").plus(documents.data?.get("lastName").toString()).plus("!!")
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