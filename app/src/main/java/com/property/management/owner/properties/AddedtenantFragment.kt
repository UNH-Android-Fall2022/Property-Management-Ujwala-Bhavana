package com.property.management.owner.properties

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


        binding.txtName.setText("name")
        binding.textEmailid.setText("")
        binding.textPhonenum.setText("")
        binding.textRentt.setText("")
        binding.textLeasedate.setText("")
        binding.textLeaseCycle.setText("")

        return root
    }

}