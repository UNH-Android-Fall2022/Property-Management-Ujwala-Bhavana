package com.property.management.tenant.maintenance_request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.property.management.databinding.FragmentMaintenanceRequesttenantBinding
import com.property.management.tenant.maintenance_request.MaintenanceRequestFragmentTenantDirections


class MaintenanceRequestFragmentTenant : Fragment() {

    private var _binding: FragmentMaintenanceRequesttenantBinding? = null
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var listOfRequests: ArrayList<PastRequestData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val maintenanceRequestViewModel =
            ViewModelProvider(this).get(MaintenanceRequestViewModel::class.java)

        _binding = FragmentMaintenanceRequesttenantBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.floatingActionReqMaintenance.setOnClickListener {
            Log.d(TAG, "Request Maintenance Button clicked")
            val action =
                MaintenanceRequestFragmentTenantDirections.actionNavigationMaintenanceRequestToNavigationCreateRequest()
            findNavController().navigate(action)
        }
        Log.d(TAG, "Calling maintenance request database...")
        listOfRequests = arrayListOf()
        readFromFirestore()
        return root
    }
    private fun readFromFirestore(){
        db.collection("Maintenance Request")
            .get()
            .addOnCompleteListener { snapshot ->
                for (document in snapshot.result) {
                    Log.d(TAG, "${document.getData()}")
                    val temp = document.getData()
                    val req: PastRequestData = PastRequestData(
                        document.id,
                        temp.get("image").toString(),
                        temp.get("subject").toString(),
                        temp.get("description").toString(),
                        temp.get("ownerId").toString(),
                        temp.get("propertyId").toString(),
                        temp.get("unitId").toString(),
                        temp.get("status").toString(),
                        temp.get("tenantId").toString()
                    )
                    listOfRequests.add(req)
                    //listOfRequests.add(document.toObject<PastRequestData>())
                }
                mRecyclerView = binding.pastRequestRecyclerViewList
                mRecyclerView.layoutManager = LinearLayoutManager(context)
                mRecyclerView.adapter = PastRequestAdapter(listOfRequests, this)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
