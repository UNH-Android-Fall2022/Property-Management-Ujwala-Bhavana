package com.property.management.tenant.property_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
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
    private val args : PropertyDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(PropertyDetailsViewModel::class.java)

        _binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db.collection("Owners").document(args.ownerID).collection("Properties").document(args.propertyID)
            .get()
            .addOnSuccessListener { property ->
                callUnitCollection()
                val address1: TextView = binding.textAddress1Value
                address1.text = property.data?.get("propertyName").toString()

                val city: TextView = binding.textCityValue
                city.text = property.data?.get("city").toString()

                val state: TextView = binding.textStateValue
                state.text = property.data?.get("state").toString()

                val zipCode: TextView = binding.textZipCodeValue
                zipCode.text = property.data?.get("zipCode").toString()
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG,"Error getting documents", exception)
            }
        //Log.d(TAG,"Unit collections called...$unitID")

        return root
    }
    private fun callUnitCollection(){
        db.collection("Owners").document(args.ownerID).collection("Properties").document(args.propertyID).collection("Units").document(args.unitID)
            .get()
            .addOnSuccessListener{unit ->
                val unitName: TextView = binding.textAddress2Value
                unitName.text = unit.data?.get("Unit Name").toString()
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