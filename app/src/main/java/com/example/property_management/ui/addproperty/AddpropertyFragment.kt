package com.example.property_management.ui.addproperty

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.property_management.databinding.FragmentAddpropertyBinding
import com.example.property_management.ui.properties.PropertiesViewModel

class AddpropertyFragment:Fragment() {
    private var _binding: FragmentAddpropertyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentAddpropertyBinding.inflate(inflater,container,false)
        val root: View = binding.root

        val selectImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.imageView.setImageURI(it)
            })

        binding.btnGallery.setOnClickListener {
            selectImage.launch("Image/*")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}