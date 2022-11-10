package com.example.tenantview_android_f22.ui.maintenance_request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tenantview_android_f22.databinding.FragmentMaintenanceRequestBinding
import com.example.tenantview_android_f22.ui.maintenance_request.MaintenanceRequestViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.ListView

class MaintenanceRequestFragment : Fragment() {

    private var _binding: FragmentMaintenanceRequestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val maintenanceRequestViewModel =
            ViewModelProvider(this).get(MaintenanceRequestViewModel::class.java)

        _binding = FragmentMaintenanceRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: ListView = binding.maintenanceRequestList
        val listOfRequests: MutableList<String> = mutableListOf()

        Log.d(TAG,"Calling maintenance request database...")
        db.collection("Maintenance Request")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d(TAG,"${document.id} => ${document.data}")
                    listOfRequests.add(document.data.toString())
                }
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
