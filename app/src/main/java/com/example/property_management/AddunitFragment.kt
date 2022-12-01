package com.example.property_management

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
import androidx.navigation.fragment.navArgs
import com.example.property_management.databinding.FragmentAddunitBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

class AddunitFragment:Fragment() {
    private var _binding: FragmentAddunitBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val args:AddunitFragmentArgs by navArgs()
    private var propertyName:String = ""

    private lateinit var imgURI: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddunitBinding.inflate(inflater,container,false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Upload Image
        val selectImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imgURI = it!!
                binding.imageViewUnit.setImageURI(it)
            })
        binding.btnUploadUnit.setOnClickListener {
            selectImage.launch("image/*")
        }

        propertyName = args.propertyName.trim()
        //Listener to Cancel button
        binding.btnCancelUnit.setOnClickListener{
            val action = AddunitFragmentDirections.actionAddunitFragmentToUnitsFragment(propertyName)
            Log.d("Test","Add Unit Fragment $propertyName")
            findNavController().navigate(action)
        }

        //Listener to Add Unit button
        binding.btnAddUnit.setOnClickListener{

            val unitData = UnitData(
                imgURL = "",
                binding.txtUnitName.text.toString(),
                binding.txtUnittype.text.toString(),
                binding.txtSqft.text.toString().toInt()
            )

            writeToFirebase(unitData)


        }
    }

    private fun writeToFirebase(unitData: UnitData) {
        val unit = hashMapOf(
            "imguRL" to "",
            "Unit Name" to unitData.unitName,
            "Unit Type" to unitData.unitType,
            "Unit Size" to unitData.unitSize,
        )
        val md = MessageDigest.getInstance("MD5")
        val docIdProp = md.digest(propertyName.trim().toByteArray(Charsets.UTF_8)).toHex()

        val docIdUnit = md.digest(unitData.unitName.trim().toByteArray(Charsets.UTF_8)).toHex()

        Log.d("Test","AddunitFragment propertyname $propertyName")
        Log.d("Test","AddUnitFragment $docIdProp")
        val userid = auth.currentUser?.uid
        db.collection("Owners").document(userid!!).collection("Properties").document(docIdProp).collection("Units").document(docIdUnit).set(unit)
            .addOnSuccessListener {
                Log.d("Test","Unit details added to collection")
                val action = AddunitFragmentDirections.actionAddunitFragmentToUnitsFragment(propertyName)
                findNavController().navigate(action)
            }
            .addOnFailureListener{exception ->
                Log.d("Test","Error in writing document in Firebase",exception)
            }

    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}