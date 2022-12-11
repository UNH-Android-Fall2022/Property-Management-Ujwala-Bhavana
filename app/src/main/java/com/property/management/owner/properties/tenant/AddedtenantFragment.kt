package com.property.management.owner.properties.tenant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar.Tab
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.property.management.databinding.FragmentAddedtenantBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

class AddedtenantFragment:Fragment() {
    private var _binding: FragmentAddedtenantBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private var tenantId = ""
    private var propertyName = ""
    private var unitName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddedtenantBinding.inflate(inflater,container,false)
        val root:View = binding.root

        val bundle = Bundle(this.arguments)
        if(bundle!=null)
        {
            propertyName = bundle.get("propName").toString()
            unitName = bundle.get("unitName").toString()
            tenantId = bundle.get("tenantId").toString()
        }

        db.collection("Tenants").document(tenantId).get()
            .addOnCompleteListener{document ->
                val m = document.result.getData()
                val name = m!!.get("firstName").toString() + " " + m.get("lastName").toString()
                binding.txtName.setText(name)
                binding.textEmailid.setText(m.get("email").toString())
                binding.textPhonenum.setText(m.get("phoneNumber").toString())
                binding.textRentt.setText(m.get("rent").toString())
                binding.textLeasedate.setText(m.get("leaseStartDate").toString())
                binding.textLeaseCycle.setText(m.get("leaseCycle").toString())
            }
            .addOnFailureListener{
                Log.d("test","Error in getting Tenant details from Firebase")
            }

        binding.btndeletetenant.setOnClickListener {
            val md = MessageDigest.getInstance("MD5")
            val propId = md.digest(propertyName.trim().toByteArray(Charsets.UTF_8)).toHex()
            val unitId = md.digest(unitName.trim().toByteArray(Charsets.UTF_8)).toHex()
            db.collection("Owners").document(auth.currentUser!!.uid).collection("Properties").document(propId).collection("Units").document(unitId)
                .update("tenantId","")
            val action = TablayoutFragmentDirections.actionTablayoutFragmentToAddtenantFragment(propertyName,unitName,"")
            findNavController().navigate(action)
            val toast = Toast.makeText(activity,"Tenant has been Deleted. You can add new Tenant here or Click Cancel",Toast.LENGTH_LONG)
            toast.show()
        }
        return root
    }

    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }

}