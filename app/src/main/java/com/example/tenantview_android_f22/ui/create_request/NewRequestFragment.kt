package com.example.tenantview_android_f22.ui.create_request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tenantview_android_f22.databinding.CreateMaintenanceRequestBinding
import com.example.tenantview_android_f22.ui.maintenance_request.PastRequestData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewRequestFragment : Fragment() {

    private var _binding: CreateMaintenanceRequestBinding? = null
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        return root
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