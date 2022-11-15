package com.example.tenantview_android_f22.ui.my_profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tenantview_android_f22.R
import com.example.tenantview_android_f22.databinding.FragmentMyProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class my_profile : Fragment() {

    companion object {
        fun newInstance() = my_profile()
    }
    private var _binding: FragmentMyProfileBinding? = null
    private lateinit var viewModel: MyProfileViewModel
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}