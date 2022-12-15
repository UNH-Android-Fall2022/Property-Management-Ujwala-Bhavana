package com.property.management.tenant.view_past_request
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.property.management.databinding.FragmentViewEachPastRequestBinding
import com.property.management.tenant.maintenance_request.PastRequestData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.GlideApp
import java.io.ByteArrayOutputStream

class ViewPastRequestFragment : Fragment() {

    private var _binding: FragmentViewEachPastRequestBinding? = null
    val args: ViewPastRequestFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(ViewPastRequestViewModel::class.java)

        _binding = FragmentViewEachPastRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val subjectParam: EditText = binding.textSubjectValue
        subjectParam.setText(args.subject)
        val descriptionParam: EditText = binding.textDescriptionValue
        descriptionParam.setText(args.description)
        val imageParam : ImageView = binding.imageValue
        GlideApp.with(requireContext()).load(args.image).into(imageParam)

        binding.save.setOnClickListener{
            writeToFirebase()
        }
        binding.closeRequest.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to close the request?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    Log.d(TAG,"yes is selected!!")
                    db.collection("Maintenance Request").document(args.documentID).update("status","Closed")
                        .addOnSuccessListener { document ->
                            Log.d(TAG,"Maintenance request closed")
                            val action = ViewPastRequestFragmentDirections.actionViewPastRequestFragmentToNavigationMaintenanceRequest()
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
    private fun writeToFirebase(){
        val image = ""
        val subject = binding.textSubjectValue.text.toString()
        val description = binding.textDescriptionValue.text.toString()
        val docID = args.documentID
        db.collection("Maintenance Request").document(docID).update("image",image,"subject",subject,"description",description)
            .addOnSuccessListener { document ->
                Log.d(TAG,"Maintenance request updated to collection: ${document}")
                val action = ViewPastRequestFragmentDirections.actionViewPastRequestFragmentToNavigationMaintenanceRequest()
                findNavController().navigate(action)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG,"Error in writing document in Firebase",exception)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}