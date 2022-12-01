package com.example.property_management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.property_management.databinding.FragmentAddtenantBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AddtenantFragment: Fragment() {
    private var _binding: FragmentAddtenantBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val args : AddtenantFragmentArgs by navArgs()

    private var propertyName =""
    private var unitName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddtenantBinding.inflate(inflater, container, false)
        val root:View = binding.root


        binding.btnAddTenant.setOnClickListener{
            propertyName = args.propertyName
            Log.d("Addtenantfragment","Add tenant button listener")
            unitName = args.unitName
            //CoroutineScope(IO).launch {
                writeTenantToFirebase()
            //}

            val action = AddtenantFragmentDirections.actionAddtenantFragmentToTablayoutFragment(propertyName,unitName)
            findNavController().navigate(action)
        }

        return root
    }

    private fun writeTenantToFirebase() {
        val tenantEmail = binding.tenantEmail.text.toString()
        Log.d("AddTenantFragment","propname $propertyName")
        Log.d("AddtenantFragment","unitnmae $unitName")
        val md = MessageDigest.getInstance("MD5")
        val docIdUnit = md.digest(unitName.trim().toByteArray(Charsets.UTF_8)).toHex()
        val docIdProp = md.digest(propertyName.trim().toByteArray(Charsets.UTF_8)).toHex()
        Log.d("Addtenentfragment","propid $docIdProp")
        Log.d("AddTenantFragment","unitid $docIdUnit")



        val tenant = hashMapOf(
            "Name" to binding.tenantName.text.toString(),
            "Email" to binding.tenantEmail.text.toString(),
            "Phone Number" to binding.tenantPhone.text.toString(),
            "Rent" to binding.Rent.text.toString(),
            "Lease Start Date" to binding.leaseStartdate.text.toString(),
            "Lease Cycle" to binding.leaseCycle.text.toString(),
            "ownerid" to auth.currentUser?.uid,
            "propertyid" to docIdProp,
            "unitid" to docIdUnit
        )

        val docId = md.digest(tenantEmail.trim().toByteArray(Charsets.UTF_8)).toHex()

        val unitdoc = db.collection("Owners").document(auth.currentUser!!.uid).collection("Properties").document(docIdProp).collection("Units").document(docIdUnit)
        unitdoc.update("tenantid",docId)

        db.collection("Tenants").document(docId).set(tenant)
            .addOnSuccessListener {
                Log.d("Test","Tenant details added succesfully to Firebase")
            }
            .addOnFailureListener{
                Log.d("Test","Error in adding Tenant to Firebase",it)
            }
    }

    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}