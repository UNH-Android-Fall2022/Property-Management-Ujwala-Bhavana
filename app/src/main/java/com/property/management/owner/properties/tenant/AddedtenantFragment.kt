package com.property.management.owner.properties.tenant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.property.management.databinding.FragmentAddedtenantBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddedtenantFragment:Fragment() {
    private var _binding: FragmentAddedtenantBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var tenantId = ""

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


        return root
    }

}