package com.example.property_management


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment, private val propName: String, private val unitName: String ): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->{
               val t = TenantFragment()
               val bundle = Bundle()
               bundle.putString("propName", propName)
               bundle.putString("unitName", unitName)
               t.arguments = bundle
               t
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