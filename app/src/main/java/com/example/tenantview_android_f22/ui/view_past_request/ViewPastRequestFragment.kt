package com.example.tenantview_android_f22.ui.view_past_request
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tenantview_android_f22.databinding.FragmentViewEachPastRequestBinding
import com.example.tenantview_android_f22.ui.create_request.NewRequestFragmentDirections
import com.example.tenantview_android_f22.ui.maintenance_request.PastRequestData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewPastRequestFragment : Fragment() {

    private var _binding: FragmentViewEachPastRequestBinding? = null
    val args: ViewPastRequestFragmentArgs by navArgs()
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
            ViewModelProvider(this).get(ViewPastRequestViewModel::class.java)

        _binding = FragmentViewEachPastRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val subjectParam: EditText = binding.textSubjectValue
        subjectParam.setText(args.subject)
        val descriptionParam: EditText = binding.textDescriptionValue
        descriptionParam.setText(args.description)

        binding.save.setOnClickListener{
            val pastRequestData = PastRequestData(
                args.documentID,
                "",
                binding.textSubjectValue.text.toString(),
                binding.textDescriptionValue.text.toString(),
                ""
            )
            writeToFirebase(pastRequestData)
        }

        return root
    }
    private fun writeToFirebase(pastRequestData: PastRequestData){
        val req = hashMapOf(
            "image" to "",
            "subject" to pastRequestData.d_subject,
            "Description" to pastRequestData.d_description,
            "tenant_ID" to pastRequestData.d_tenant_id
        )
        val docID = pastRequestData.d_id
        db.collection("Maintenance Request").document(docID).set(req)
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