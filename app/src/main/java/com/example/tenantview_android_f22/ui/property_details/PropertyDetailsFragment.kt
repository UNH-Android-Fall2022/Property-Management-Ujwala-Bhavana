package com.example.tenantview_android_f22.ui.property_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tenantview_android_f22.databinding.FragmentPropertyDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class PropertyDetailsFragment : Fragment() {

    private var _binding: FragmentPropertyDetailsBinding? = null

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
        val chatViewModel =
            ViewModelProvider(this).get(PropertyDetailsViewModel::class.java)

        _binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var propertyName = ""
        Log.d(TAG,"Calling the Property Details database...")
        db.collection("Property_Details_Test")
            .get()
            .addOnSuccessListener { listOfProperties ->
                for (property in listOfProperties){
                    Log.d(TAG,"${property.id} => ${property.data}")

                    val address1: TextView = binding.textAddress1Value
                    address1.text = property.data["propertyName"].toString()

                    val city: TextView = binding.textCityValue
                    city.text = property.data["city"].toString()

                    val state: TextView = binding.textStateValue
                    state.text = property.data["state"].toString()

                    val zipCode: TextView = binding.textZipCodeValue
                    zipCode.text = property.data["zipCode"].toString()
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