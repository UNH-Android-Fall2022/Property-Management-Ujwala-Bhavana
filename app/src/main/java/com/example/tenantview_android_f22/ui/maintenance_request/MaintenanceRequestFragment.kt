package com.example.tenantview_android_f22.ui.maintenance_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tenantview_android_f22.databinding.FragmentMaintenanceRequestBinding
import com.example.tenantview_android_f22.ui.chat.MaintenanceRequestViewModel

class MaintenanceRequestFragment : Fragment() {

    private var _binding: FragmentMaintenanceRequestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val maintenanceRequestViewModel =
            ViewModelProvider(this).get(MaintenanceRequestViewModel::class.java)

        _binding = FragmentMaintenanceRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMaintenanceRequest
        maintenanceRequestViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}