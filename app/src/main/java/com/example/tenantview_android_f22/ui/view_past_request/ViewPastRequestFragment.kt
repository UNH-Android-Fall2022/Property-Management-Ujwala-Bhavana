package com.example.tenantview_android_f22.ui.view_past_request
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tenantview_android_f22.databinding.FragmentViewEachPastRequestBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewPastRequestFragment : Fragment() {

    private var _binding: FragmentViewEachPastRequestBinding? = null

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
        val bundle = arguments
        val temp1 = bundle?.getString("temp1")
        Log.d(TAG,"from view past request fetch temp1= $temp1")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}