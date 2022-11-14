package com.example.property_management.ui.properties

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.property_management.PropertyData
import com.example.property_management.databinding.FragmentPropertiesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PropertiesFragment : Fragment() {

    private var _binding: FragmentPropertiesBinding? = null
    private lateinit var pRecyclerView:RecyclerView
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var propertyList: ArrayList<PropertyData>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(PropertiesViewModel::class.java)

        _binding = FragmentPropertiesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        */

        propertyList = arrayListOf()
        readFromFirestore()

        // Log.d("Test","Size of propertyList ${propertyList.size}")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun readFromFirestore() {
        // val db = Firebase.firestore
        db = FirebaseFirestore.getInstance()
        val auth = Firebase.auth
        val userid = auth.currentUser?.uid

        db.collection("Owners").document(userid!!).collection("Properties").get()
            .addOnCompleteListener { result ->
                for (document in result.getResult()) {
                    if (document.data.get("propertyName") != "") {
                        Log.d("Test", "${document.id} => ${document.data}")
                        propertyList.add(
                            document.toObject<PropertyData>()
                        )
                    }
                }
                pRecyclerView = binding.recyclerViewProperties
                // pRecyclerView.setHasFixedSize(true)
                pRecyclerView.layoutManager = LinearLayoutManager(context)
                propertyAdapter = PropertyAdapter(propertyList,this)
                pRecyclerView.adapter = propertyAdapter


                binding.btnAdd.setOnClickListener{
                    Log.d("Test","Add property button selected")
                    val action = PropertiesFragmentDirections.actionNavigationPropertiesToAddpropertyFragment()
                    findNavController().navigate(action)
                }
                Log.d("Test", "${propertyList.size}")
            }
            .addOnFailureListener { exception ->
                Log.d("Test", "Error getting documents", exception)
            }
    }

        /*
        db.collection("Owners").document(userid!!).collection("properties")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("Test", "Listen Failed", e)
                }
                if (snapshot != null) {
                    Log.d("Test", "Snapshot not empty")
                    for (document in snapshot) {
                        propertyList.add(
                            document.toObject<PropertyData>()
                        )
                    }
                    Log.d("Test", propertyList.size.toString());
                } else {
                    Log.d("Test", "document null")
                }
            }
    }

         */
}