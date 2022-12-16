package com.property.management.owner.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.property.management.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.property.management.databinding.FragmentAccountownerBinding
import com.property.management.databinding.FragmentAccounttenantBinding

class AccountFragmentOwner: Fragment(){

    private var _binding: FragmentAccountownerBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountownerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textlogout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(activity,MainActivity::class.java))

        }
        binding.textprofile.setOnClickListener {
            val action = AccountFragmentOwnerDirections.actionNavigationAccountOwnerToProfileFragment()
            findNavController().navigate(action)
        }
        return root
    }

}