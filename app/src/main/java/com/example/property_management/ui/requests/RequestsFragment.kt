package com.example.property_management.ui.requests

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.property_management.MaintenanceRequestData
import com.example.property_management.RequestAdapter
import com.example.property_management.databinding.FragmentRequestsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class RequestsFragment : Fragment() {

    private var _binding: FragmentRequestsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var requestList: ArrayList<MaintenanceRequestData>
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var rRecyclerView :RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requestList = arrayListOf()
        Log.d("RequestsFragment","test")
        readRequestsFromFirestore()


        return root
    }

    private fun readRequestsFromFirestore() {
        val ownerid = auth.currentUser?.uid
        db.collection("Maintenance Request").whereEqualTo("ownerid",ownerid).get()
            .addOnCompleteListener { documents ->
                for(document in documents.result){
                    requestList.add(document.toObject<MaintenanceRequestData>())}

                Log.d("RequestFragment","requestlist size ${requestList.size}")
                Log.d("RequestFragment","${requestList[0]}")
                rRecyclerView = binding.recyclerViewRequests
                rRecyclerView.layoutManager = LinearLayoutManager(context)
                requestAdapter = RequestAdapter(requestList,this)
                rRecyclerView.adapter = requestAdapter

            }

            .addOnFailureListener{
                Log.d("RequestsFragment","Error in getting requests from Firebase",it)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}