package com.property.management.owner.properties.tenant

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.property.management.databinding.FragmentTenantBinding

class TenantFragment : Fragment() {
    private var _binding: FragmentTenantBinding? = null
    private val binding get() = _binding!!
    var propertyName :String = ""
    var unitName :String =""
    var tenantId: String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTenantBinding.inflate(inflater,container,false)
        val root:View = binding.root

        var tablayoutFragment = TablayoutFragment()
        binding.btnAddTenan.setOnClickListener{view->
            //val tabs = (TabActivity) getParent();
            Log.d("TenantFragment","Add Tenant button clicked")

            val bundle = Bundle(this.arguments)
            if(bundle!=null)
            {
                 propertyName = bundle.get("propName").toString()
                unitName = bundle.get("unitName").toString()
                tenantId = bundle.get("tenantId").toString()
            }
            Log.d("TenantFragment","propertyname $propertyName")
            Log.d("TenantFragment","unitnmae $unitName")

            val action = TablayoutFragmentDirections.actionTablayoutFragmentToAddtenantFragment(propertyName,unitName,tenantId)
            view.findNavController().navigate(action)

        }
        return root

    }


}