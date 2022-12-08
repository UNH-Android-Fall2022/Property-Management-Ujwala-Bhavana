package com.property.management.tenant.property_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.property.management.databinding.FragmentPropertyDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PropertyDetailsFragment : Fragment() {

    private var _binding: FragmentPropertyDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private var unitID = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(PropertyDetailsViewModel::class.java)

        _binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.d(TAG,"Calling the Property Details database...")
        db.collection("Property_Details_Test")
            .get()
            .addOnSuccessListener { listOfProperties ->
                for (property in listOfProperties){
                    Log.d(TAG,"${property.id} => ${property.data}")
                    unitID = property.id
                    callUnitCollection(unitID)
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
        //Log.d(TAG,"Unit collections called...$unitID")

        return root
    }
    private fun callUnitCollection(unitId: String){
        unitID = unitId
        db.collection("Property_Details_Test").document(unitID).collection("Unit")
            .get()
            .addOnCompleteListener{snapshot ->
                for(unit in snapshot.result) {
                    val temp = unit.getData()
                    Log.d(TAG, "Unit name: ${unit.getData()}")
                    val unitName: TextView = binding.textAddress2Value
                    unitName.text = temp.get("unitName").toString()
                }
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}