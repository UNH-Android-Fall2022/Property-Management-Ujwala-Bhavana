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
import com.property.management.databinding.FragmentMaintenanceRequestBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.tenant.maintenance_request.MaintenanceRequestFragmentDirections


class MaintenanceRequestFragment : Fragment() {

    private var _binding: FragmentMaintenanceRequestBinding? = null
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

        _binding = FragmentMaintenanceRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.floatingActionReqMaintenance.setOnClickListener {
            Log.d(TAG, "Request Maintenance Button clicked")
            val action =
                MaintenanceRequestFragmentDirections.actionNavigationMaintenanceRequestToNavigationCreateRequest()
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
                        d_id = document.id,
                        "",
                        d_subject = temp.get("subject").toString(),
                        d_description = temp.get("Description").toString(),
                        ""
                    )
                    listOfRequests.add(req)
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
