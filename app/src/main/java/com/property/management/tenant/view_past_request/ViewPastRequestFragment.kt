package com.property.management.tenant.view_past_request
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.property.management.databinding.FragmentViewEachPastRequestBinding
import com.property.management.tenant.maintenance_request.PastRequestData
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
                image = args.image,
                ownerid = args.ownerid,
                propertyname = args.propertyname,
                unitname = args.unitname,
                tenantid = args.tenantid,
                subject = binding.textSubjectValue.text.toString(),
                description = binding.textDescriptionValue.text.toString(),
                status = args.status
            )
            writeToFirebase(pastRequestData)
        }
        binding.delete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    Log.d(TAG,"yes is selected!!")
                    db.collection("Maintenance Request").document(args.documentID).delete()
                        .addOnSuccessListener { document ->
                            Log.d(TAG,"Maintenance request deleted from collection:" +
                                    "" +
                                    "")
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