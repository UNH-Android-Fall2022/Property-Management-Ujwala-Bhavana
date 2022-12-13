package com.property.management.tenant.create_request

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.property.management.databinding.CreateMaintenanceRequestBinding
import com.property.management.tenant.maintenance_request.PastRequestData
import java.io.ByteArrayOutputStream
import java.security.MessageDigest


class NewRequestFragment : Fragment() {

    private var _binding: CreateMaintenanceRequestBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var storageRef = Firebase.storage
    private var imgURL = ""
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

        val md = MessageDigest.getInstance("MD5")
        val docId = md.digest(auth.currentUser!!.email!!.trim().toByteArray()).toHex()

        binding.save.setOnClickListener{
            var ownerId = ""
            var propertyName = ""
            var unitName = ""
            db.collection("Tenants").document(docId)
                .get()
                .addOnSuccessListener { documents ->
                    ownerId = documents.data?.get("ownerId").toString()
                    propertyName = documents.data?.get("propertyName").toString()
                    unitName = documents.data?.get("unitName").toString()
                    val pastRequestData = PastRequestData(
                        "",
                        imgURL,
                        subject = binding.editSubject.text.toString(),
                        description = binding.editDescription.text.toString(),
                        ownerId = ownerId,
                        propertyName = propertyName,
                        unitName = unitName,
                        status = "Open",
                        tenantId = docId
                    )
                    writeToFirebase(pastRequestData)
                }
                .addOnFailureListener{ exception ->
                    Log.w(TAG,"Error getting documents", exception)
                }
        }
        binding.uploadImage.setOnClickListener{
            val camera_intent =
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera_intent, picID)
        }
        /*binding.maintenanceRequestImageView.setOnClickListener{
            Log.d(TAG,"image view clicked")
            /*val image : ImageView = binding.maintenanceRequestImageView
            val builder = AlertDialog.Builder(context)
            builder.setView(image)
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()*/

            val imageDialog = AlertDialog.Builder(context)
            imageDialog.setTitle("Title")
            val showImage : ImageView = binding.maintenanceRequestImageView
            imageDialog.setView(showImage)

            imageDialog.setNegativeButton(
                "Close"
            ) { arg0, arg1 -> }
            imageDialog.show()

        }*/
        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == picID) {
            val photo = data!!.extras!!["data"] as Bitmap?
            binding.maintenanceRequestImageView.setImageBitmap(photo)
            val bytes = ByteArrayOutputStream()
            photo!!.compress(Bitmap.CompressFormat.JPEG,90, bytes)
            val byteArray = bytes.toByteArray()
            uploadToFirebase(byteArray)
        }
    }

    private fun uploadToFirebase(byteArray: ByteArray) {
        storageRef.getReference("MaintenanceReqImages").child(System.currentTimeMillis().toString())
            .putBytes(byteArray)
            .addOnSuccessListener { task->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                        imgURL = it.toString()
                    }
            }
    }

    private fun writeToFirebase(pastRequestData: PastRequestData){
        val req = hashMapOf(
            "image" to pastRequestData.image,
            "subject" to pastRequestData.subject,
            "description" to pastRequestData.description,
            "ownerId" to pastRequestData.ownerId,
            "propertyName" to pastRequestData.propertyName,
            "unitName" to pastRequestData.unitName,
            "status" to pastRequestData.status,
            "tenantId" to pastRequestData.tenantId
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
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}