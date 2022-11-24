package com.example.property_management

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.property_management.databinding.FragmentTablayoutBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class TablayoutFragment : Fragment() {
    private var _binding: FragmentTablayoutBinding? = null
    private val binding get() = _binding!!
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
        val adapter = ViewPagerAdapter(this)

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


