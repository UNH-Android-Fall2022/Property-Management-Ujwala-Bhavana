package com.example.property_management

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.property_management.databinding.FragmentUnitsBinding


class UnitsFragment : Fragment() {
    private var _binding: FragmentUnitsBinding? = null
    private val binding get() = _binding!!
    val args : UnitsFragmentArgs by navArgs()
    private var propertyName:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnitsBinding.inflate(inflater,container,false)
        val root: View = binding.root
        propertyName = args.propertyName
        binding.btnAddUnit.setOnClickListener{
            Log.d("Test","Units Fragment $propertyName")
            val action = UnitsFragmentDirections.actionUnitsFragmentToAddunitFragment(propertyName)
            findNavController().navigate(action)
        }
        return root
    }

    


}