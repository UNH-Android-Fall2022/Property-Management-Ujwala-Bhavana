package com.property.management.owner.properties

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.property.management.databinding.FragmentAddtenantBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

            val action =
                AddtenantFragmentDirections.actionAddtenantFragmentToTablayoutFragment(
                    propertyName,
                    unitName
                )
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
            "firstName" to binding.txtfirstname.text.toString(),
            "lastName" to binding.txtlastname.text.toString(),
            "email" to binding.tenantEmail.text.toString(),
            "phoneNumber" to binding.tenantPhone.text.toString(),
            "rent" to binding.Rent.text.toString(),
            "leaseStartDate" to binding.leaseStartdate.text.toString(),
            "leaseCycle" to binding.leaseCycle.text.toString(),
            "ownerId" to auth.currentUser?.uid,
            "propertyId" to docIdProp,
            "unitId" to docIdUnit,
            "propertyName" to propertyName,
            "unitName" to unitName
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