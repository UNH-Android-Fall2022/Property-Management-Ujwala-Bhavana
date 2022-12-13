package com.property.management.owner.properties.tenant

import android.app.DatePickerDialog
import android.icu.util.Calendar
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
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AddtenantFragment: Fragment() {
    private var _binding: FragmentAddtenantBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val args : com.property.management.owner.properties.tenant.AddtenantFragmentArgs by navArgs()

    private var propertyName =""
    private var unitName = ""
    private var tenantId = ""
    val calendar = Calendar.getInstance()
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


        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        binding.leaseStartdate.setOnClickListener {
            DatePickerDialog(requireContext(), datePicker, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show()

        }
        binding.btnAddTenant.setOnClickListener{
            propertyName = args.propertyName
            Log.d("Addtenantfragment","Add tenant button listener")
            unitName = args.unitName
            val md = MessageDigest.getInstance("MD5")
            val docId = md.digest(binding.tenantEmail.text.toString().trim().toByteArray(Charsets.UTF_8)).toHex()
            //CoroutineScope(IO).launch {
                writeTenantToFirebase()
            //}

        }
        binding.btntenantcancel.setOnClickListener {
            tenantId = ""
            val action = AddtenantFragmentDirections.actionAddtenantFragmentToTablayoutFragment(propertyName,unitName,tenantId)
            findNavController().navigate(action)
        }
        return root
    }

    private fun updateDateInView() {
        val format = "MM/dd/YYYY"
        val sdf = SimpleDateFormat(format, Locale.US)
        binding.leaseStartdate.setText(sdf.format(calendar.time))
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

        var leaseCycle = ""

        if(binding.rbtnmonthly.isChecked)
            leaseCycle = "Monthly"
        else if(binding.rbtnyearly.isChecked)
            leaseCycle = "Yearly"



        val tenant = hashMapOf(
            "firstName" to binding.txtfirstname.text.toString(),
            "lastName" to binding.txtlastname.text.toString(),
            "email" to binding.tenantEmail.text.toString(),
            "phoneNumber" to binding.tenantPhone.text.toString(),
            "rent" to binding.Rent.text.toString().toInt(),
            "leaseStartDate" to Date(binding.leaseStartdate.text.toString()),
            "leaseCycle" to leaseCycle,
            "ownerId" to auth.currentUser?.uid,
            "propertyId" to docIdProp,
            "unitId" to docIdUnit,
            "propertyName" to propertyName,
            "unitName" to unitName
        )

        val docId = md.digest(tenantEmail.trim().toByteArray(Charsets.UTF_8)).toHex()


        db.collection("Tenants").document(docId).set(tenant)
            .addOnSuccessListener {
                Log.d("Test","Tenant details added succesfully to Firebase")
                val action =
                    com.property.management.owner.properties.tenant.AddtenantFragmentDirections.actionAddtenantFragmentToTablayoutFragment(
                        propertyName,
                        unitName,
                        docId
                    )
                findNavController().navigate(action)
            }
            .addOnFailureListener{
                Log.d("Test","Error in adding Tenant to Firebase",it)
            }
        val unitdoc = db.collection("Owners").document(auth.currentUser!!.uid).collection("Properties").document(docIdProp).collection("Units").document(docIdUnit)
        unitdoc.update("tenantId",docId)
    }

    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}