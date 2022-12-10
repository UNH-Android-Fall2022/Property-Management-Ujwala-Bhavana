package com.property.management.owner.properties

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.property.management.databinding.FragmentUnitsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest


class UnitsFragment : Fragment() {

    private var _binding: FragmentUnitsBinding? = null
    private val binding get() = _binding!!

    private lateinit var uRecyclerView: RecyclerView
    private lateinit var unitAdapter: UnitAdapter
    private lateinit var unitList: ArrayList<UnitData>
    private val db = Firebase.firestore
    private val auth = Firebase.auth



    /*
    companion object{
        private val PropertyName = "zujwala"
    }
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnitsBinding.inflate(inflater,container,false)
        val root: View = binding.root

        unitList = arrayListOf()
        Log.d("Test","UnitsFragment")
        readFromFirestore()
        return root
    }

    private fun readFromFirestore() {

        val args : com.property.management.owner.properties.UnitsFragmentArgs by navArgs()
        val propertyName = args.propertyName.trim()

        val userid = auth.currentUser?.uid
        val md = MessageDigest.getInstance("MD5")
        val docId = md.digest(propertyName.trim().toByteArray(Charsets.UTF_8)).toHex()
        Log.d("Test","$ UnitsFragment $userid")
        Log.d("Test","UnitsFragment $docId")
        db.collection("Owners").document(userid!!).collection("Properties").document(docId).collection("Units").get()
            .addOnCompleteListener{snapshot ->
                Log.d("Test","OncompleteListener UnitsFragment")
                if(snapshot != null && snapshot != null)
                {

                    for(document in snapshot.result)
                    {
                        Log.d("Test","document ${document.getData()}")
                        // val unit = document.toObject(UnitData::class.java,DocumentSnapshot.ServerTimestampBehavior.PREVIOUS)

                        val m = document.getData()

                        val unit: UnitData = UnitData(
                            unitName = m.get("unitName").toString(),
                            unitSize = m.get("unitSize").toString().toInt(),
                            unitType = m.get("unitType").toString(),
                            imgUrl = m.get("imguRL").toString(),
                            tenantId = m.get("tenantId").toString()
                        )

                            // Log.d("Test", "Unit $unit")
                        unitList.add(unit)
                    }
                }


                Log.d("Unit Fragment","Size f unitList ${unitList.size}")
                uRecyclerView = binding.recyclerViewUnits
                uRecyclerView.layoutManager = LinearLayoutManager(context)
                val m = hashMapOf<String, ArrayList<UnitData>>()
                m[propertyName] = unitList
                m["units"] = unitList
                unitAdapter = UnitAdapter(m    ,this)
                uRecyclerView.adapter = unitAdapter

                binding.btnAddUnit.setOnClickListener{
                    Log.d("Unit Fragment","Units Fragment $propertyName")
                    val action =
                        com.property.management.owner.properties.UnitsFragmentDirections.actionUnitsFragmentToAddunitFragment(
                            propertyName
                        )
                    findNavController().navigate(action)
                }
            }
            .addOnFailureListener{e ->
                Log.d("Test", "Error", e)
            }



    }

    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }

}