package com.property.management.owner.properties

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.property.management.databinding.FragmentTablayoutBinding
import com.google.android.material.tabs.TabLayoutMediator


class TablayoutFragment : Fragment() {
    private var _binding: FragmentTablayoutBinding? = null
    private val binding get() = _binding!!
    val args: com.property.management.owner.properties.TablayoutFragmentArgs by navArgs()
    var propertyName = ""
    var unitName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTablayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.pager1
        propertyName = args.propertyName
        unitName = args.unitName
        val adapter = ViewPagerAdapter(this,propertyName,unitName,lifecycle)


        Log.d("TabLayoutFragemnt","$propertyName")
        Log.d("TabLayoutFragment","$unitName")
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2){ tab, position ->
            when(position){
                0->{
                    tab.text = "Tenant"
                }
                1->{
                    tab.text = "Payments"
                }
                2->{
                    tab.text = "Documents"
                }
            }

        }.attach()

        return root
    }

    }