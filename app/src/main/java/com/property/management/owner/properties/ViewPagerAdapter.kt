package com.property.management.owner.properties


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

class ViewPagerAdapter(fragment: Fragment, private val propName: String, private val unitName: String,
                     private  val lifecycle: Lifecycle): FragmentStateAdapter(fragment){
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

               val bundle = Bundle()
               bundle.putString("propName", propName)
               bundle.putString("unitName", unitName)
               var t = Fragment()
                   val flag = readUnitFromFirebase()
                   Log.d("Frag","returned value ${flag}")
                   if (flag) {
                       t = AddedtenantFragment()
                       t.arguments = bundle
                       Log.d("Frag","flag value $flag")
                   }
                   else {
                       Log.d("TenantFrag block","flag value $flag")
                       t = TenantFragment()
                       t.arguments = bundle

                   }

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

    private fun readUnitFromFirebase():Boolean {

        var flag = false
        val auth = Firebase.auth
        val db = Firebase.firestore
        val ownerId = auth.currentUser?.uid
        val md = MessageDigest.getInstance("MD5")
        val docIdUnit = md.digest(unitName.trim().toByteArray(Charsets.UTF_8)).toHex()
        val docIdProp = md.digest(propName.trim().toByteArray(Charsets.UTF_8)).toHex()

        db.collection("Owners").document(ownerId!!).collection("Properties").document(docIdProp)
            .collection("Units").document(docIdUnit).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val document = task.result
                    if (document != null) {
                        val m = document.getData()
                        Log.d("viewPagerAdapter frag", "$m")
                        if (m != null && m!!.containsKey("tenantid"))
                            flag = true
                        Log.d("viewPagerAdapter frag", " flag $flag")
                    }

                }
            }
        return flag
    }

    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }

}