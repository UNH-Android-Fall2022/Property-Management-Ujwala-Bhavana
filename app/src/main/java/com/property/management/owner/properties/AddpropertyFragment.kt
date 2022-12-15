package com.property.management.owner.properties

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
import com.property.management.databinding.FragmentAddpropertyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
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
    private var imgURL =""
    private var storageRef = Firebase.storage
    lateinit var propertyData: PropertyData

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
        storageRef = FirebaseStorage.getInstance()

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
            val action =
                com.property.management.owner.properties.AddpropertyFragmentDirections.actionAddpropertyFragmentToNavigationProperties()
            findNavController().navigate(action)
        }

        binding.btnAddProperty.setOnClickListener{
            uploadImage()
            //TODO check if all entries all filled

        }

    }

    private fun writeToFirebase(propertyData: PropertyData) {
        val property = hashMapOf(
            "imgUrl" to propertyData.imgUrl,
            "propertyName" to propertyData.propertyName,
            "city" to propertyData.city,
            "state" to propertyData.state,
            "zipcode" to propertyData.zipcode,
            "units" to propertyData.units
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
                    val action =
                        com.property.management.owner.properties.AddpropertyFragmentDirections.actionAddpropertyFragmentToNavigationProperties()
                    findNavController().navigate(action)
                }
                .addOnFailureListener {
                    Log.d(TAG, "Error in writing property details to collection", it)
                }
        }
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }

    private fun uploadImage() {

        Log.d(TAG,"UploadImage function called")
        storageRef.getReference("images").child(System.currentTimeMillis().toString())
            .putFile(imgURI)
            .addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                        imgURL = it.toString()
                        Log.d("Test","Image URL $imgURL")
                        propertyData = PropertyData(
                            imgURL,
                            binding.txtAddress.text.toString(),
                            binding.txtCity.text.toString(),
                            binding.txtState.text.toString(),
                            binding.txtZipcode.text.toString().toInt(),
                            binding.txtUnits.text.toString().toInt()
                        )
                        // Write to firestore
                        writeToFirebase(propertyData)
                    }
                    .addOnFailureListener{error ->
                        Log.d("Test","Error in getting image URL")

                    }
            }
            .addOnFailureListener{exception ->
                Log.d("Test","Error in uploading image to Firebase",exception)
            }
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }*/

}