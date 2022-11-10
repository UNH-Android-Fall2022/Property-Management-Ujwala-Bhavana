package com.example.tenantview_android_f22.ui.maintenance_request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.tenantview_android_f22.databinding.FragmentMaintenanceRequestBinding
import com.example.tenantview_android_f22.ui.create_request.NewRequestFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



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

        binding.maintenanceRequestButton.setOnClickListener{
            Log.d(TAG,"Request Maintenance Button clicked")
            val action = MaintenanceRequestFragmentDirections.actionNavigationMaintenanceRequestToNavigationCreateRequest()
            findNavController().navigate(action)


        }

        val requestListView: ListView = binding.pastRequestList
        val listOfRequests: MutableList<String> = ArrayList()
        val arrayAdapter: ArrayAdapter<*>

        Log.d(TAG,"Calling maintenance request database...")
        db.collection("Maintenance Request")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    Log.d(TAG,"${document.id} => ${document.data}")
                    listOfRequests.add(document.data["subject"].toString())

                }
                Log.d(TAG,"$listOfRequests")
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
        arrayAdapter = ArrayAdapter(requireActivity(),
            android.R.layout.simple_list_item_1, listOfRequests)
        requestListView.adapter = arrayAdapter
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
