package com.example.property_management


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment:Fragment ) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->{
               TenantFragment()
           }
           1->{
               PaymentsFragment()
           }
           2->{
               DocumentsFragment()
           }
           else->{
               Fragment()
           }

       }
    }

}