package com.property.management.tenant.create_request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.property.management.databinding.CreateMaintenanceRequestBinding
import com.property.management.tenant.maintenance_request.PastRequestData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewRequestFragment : Fragment() {

    private var _binding: CreateMaintenanceRequestBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var cameraID: Button
    lateinit var clickID: ImageView
    companion object {
        // Define the pic id
        private const val picID = 123
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(NewRequestViewModel::class.java)

        _binding = CreateMaintenanceRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.save.setOnClickListener{
            val pastRequestData = PastRequestData(
                "",
                "",
                binding.editSubject.text.toString(),
                binding.editDescription.text.toString(),
                ""
            )
            writeToFirebase(pastRequestData)
        }
        binding.uploadImage.setOnClickListener{
            val camera_intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            } else {
                TODO("VERSION.SDK_INT < CUPCAKE")
            }
            startActivityForResult(camera_intent, picID)
        }

        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == picID) {
            val photo = data!!.extras!!["data"] as Bitmap?
            binding.maintenanceRequestImageView.setImageBitmap(photo)
        }
    }
    private fun writeToFirebase(pastRequestData: PastRequestData){
        val req = hashMapOf(
            "image" to "",
            "subject" to pastRequestData.d_subject,
            "Description" to pastRequestData.d_description,
            "tenant_ID" to pastRequestData.d_tenant_id
        )
        db.collection("Maintenance Request").add(req)
            .addOnSuccessListener { document ->
                Log.d(TAG,"Maintenance request added to collection: ${document.id}")
                val action = NewRequestFragmentDirections.actionNavigationCreateRequestToNavigationMaintenanceRequest()
                findNavController().navigate(action)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG,"Error in writing document in Firebase",exception)
            }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}