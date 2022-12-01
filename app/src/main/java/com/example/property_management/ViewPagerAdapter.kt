package com.example.property_management


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest

class ViewPagerAdapter(fragment: Fragment, private val propName: String, private val unitName: String ): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val auth = Firebase.auth
        val db = Firebase.firestore
        val ownerId = auth.currentUser?.uid
        val md = MessageDigest.getInstance("MD5")
        val docIdUnit = md.digest(unitName.trim().toByteArray(Charsets.UTF_8)).toHex()
        val docIdProp = md.digest(propName.trim().toByteArray(Charsets.UTF_8)).toHex()
       return when(position){
           0->{

               var flag = false

               val bundle = Bundle()
               bundle.putString("propName", propName)
               bundle.putString("unitName", unitName)

               var t = Fragment()

               readUnitFromFirebase(object: TenantExistCallBack {
                   override fun isTenant(exist: Boolean) {
                       Log.d("PagerAdapter frag", "$exist")
                       if (exist) {
                           t = AddedtenantFragment()
                           t.arguments = bundle
                       } else {
                           t = TenantFragment()
                           t.arguments = bundle
                       }
                   }
               })

               Log.d("Test frag", "Empty Msg")
               return t
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

    interface TenantExistCallBack {
        fun isTenant(exist: Boolean);
    }

    private fun readUnitFromFirebase(tenantExistCallBack: TenantExistCallBack) {

        var flag = false
        val auth = Firebase.auth
        val db = Firebase.firestore
        val ownerId = auth.currentUser?.uid
        val md = MessageDigest.getInstance("MD5")
        val docIdUnit = md.digest(unitName.trim().toByteArray(Charsets.UTF_8)).toHex()
        val docIdProp = md.digest(propName.trim().toByteArray(Charsets.UTF_8)).toHex()

        db.collection("Owners").document(ownerId!!).collection("Properties").document(docIdProp).collection("Units").document(docIdUnit).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val document = task.result
                    if (document != null) {
                        val m = document.getData()
                        Log.d("viewPagerAdapter frag", "$m")
                        if (m != null && m!!.containsKey("tenantid"))
                        // flag = true
                            tenantExistCallBack.isTenant(true)
                        else
                            tenantExistCallBack.isTenant(false)
                        Log.d("viewPagerAdapter frag", " flag $flag")
                    }

                }
            }}

    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }

}