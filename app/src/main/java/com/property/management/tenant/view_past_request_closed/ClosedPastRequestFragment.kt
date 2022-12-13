package com.property.management.tenant.view_past_request_closed

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.GlideApp
import com.property.management.databinding.FrgamentViewPastRequestClosedBinding


class ClosedPastRequestFragment : Fragment() {
    private var _binding: FrgamentViewPastRequestClosedBinding? = null
    val args: ClosedPastRequestFragmentArgs by navArgs()
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
            ViewModelProvider(this).get(ClosedPastRequestViewModel::class.java)

        _binding = FrgamentViewPastRequestClosedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val subjectParam: TextView = binding.textSubjectValue
        subjectParam.setText(args.subject)
        val descriptionParam: TextView = binding.textDescriptionValue
        descriptionParam.setText(args.description)
        val imageParam : ImageView = binding.imageValue
        GlideApp.with(requireContext()).load(args.image).into(imageParam)

        binding.reopenRequest.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to reopen the request?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    Log.d(TAG,"yes is selected!!")
                    db.collection("Maintenance Request").document(args.documentID).update("status","Open")
                        .addOnSuccessListener { document ->
                            Log.d(TAG,"Maintenance request closed")
                            val action = ClosedPastRequestFragmentDirections.actionClosedPastRequestFragmentToNavigationMaintenanceRequest()
                            findNavController().navigate(action)
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG,"Error in writing document in Firebase",exception)
                        }
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}