package com.example.tenantview_android_f22.ui.view_past_request
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tenantview_android_f22.databinding.FragmentViewEachPastRequestBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewPastRequestFragment : Fragment() {

    private var _binding: FragmentViewEachPastRequestBinding? = null
    val args: ViewPastRequestFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(ViewPastRequestViewModel::class.java)

        _binding = FragmentViewEachPastRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val subjectParam = args.subject
        val descriptionParam = args.description
        binding.textSubjectValue.text = subjectParam
        binding.textDescriptionValue.text = descriptionParam

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}