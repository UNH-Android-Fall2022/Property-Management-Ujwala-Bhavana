package com.example.property_management.ui.addproperty

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.property_management.PropertyData
import com.example.property_management.databinding.FragmentAddpropertyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

class AddpropertyFragment:Fragment() {
    private var _binding: FragmentAddpropertyBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private var storage = Firebase.storage
    private lateinit var auth: FirebaseAuth
    private val TAG = "Test"
    private lateinit var imgURI:Uri

    var properties:MutableList<PropertyData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentAddpropertyBinding.inflate(inflater,container,false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        //Select Image from Gallery
        val selectImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imgURI = it!!
                binding.imageView.setImageURI(it)
            })
        binding.btnGallery.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnCancel.setOnClickListener{
            val action = AddpropertyFragmentDirections.actionAddpropertyFragmentToNavigationProperties()
            findNavController().navigate(action)
        }

        binding.btnAddProperty.setOnClickListener{
            //val imgURL = uploadImage()
            //ToDO add image to Firebase storage
            //TODO check if all entries all filled

            // Create PropertyData instance of new property
            val propertyData = PropertyData(
                    "image",
                    binding.txtAddress.text.toString(),
                    binding.txtUnits.text.toString()
            )

            // Write to firestore
            writeToFirebase(propertyData)

            val action = AddpropertyFragmentDirections.actionAddpropertyFragmentToNavigationProperties()
            findNavController().navigate(action)
        }

    }

    private fun writeToFirebase(propertyData: PropertyData) {
        val property = hashMapOf(
            "ImgURL" to propertyData.imgUrl,
            "propertyName" to propertyData.propertyName,
            "Units" to propertyData.units
        )
        val md = MessageDigest.getInstance("MD5")
        val docId = md.digest(propertyData.propertyName.trim().toByteArray(UTF_8)).toHex()

        Log.d("Test","AddPropertyFragment propertyname ${propertyData.propertyName}")
        Log.d("Test","AddPropertyFragment $docId")
        val userid = auth.currentUser?.uid
        if (userid != null) {
            db.collection("Owners").document(userid).collection("Properties").document(docId).set(property)
                .addOnSuccessListener {
                    Log.d(TAG, "Property details added to collection")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Error in writing property details to collection", it)
                }
        }
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }

    /*private fun uploadImage(): Task<Uri>? {

        Log.d(TAG,"UploadImage function called")
        val storageRef = storage.getReference("PropertyImages").child(System.currentTimeMillis().toString())
        storageRef.putFile(imgURI)
            .addOnSuccessListener{
                Log.d(TAG,"Image uploaded to Firebase storage")
            }
            .addOnFailureListener{
                Log.d(TAG,"Error in uploading image to Firebase storage",it)
            }
        return storageRef.downloadUrl
    }*/

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }*/

}