package com.property.management.tenant.view_past_request_closed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.property.management.databinding.FrgamentViewPastRequestClosedBinding


class ClosedPastRequestFragment : Fragment() {
    private var _binding: FrgamentViewPastRequestClosedBinding? = null
    val args: ClosedPastRequestFragmentArgs by navArgs()
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
            ViewModelProvider(this).get(ClosedPastRequestViewModel::class.java)

        _binding = FrgamentViewPastRequestClosedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val subjectParam: TextView = binding.textSubjectValue
        subjectParam.setText(args.subject)
        val descriptionParam: TextView = binding.textDescriptionValue
        descriptionParam.setText(args.description)
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}