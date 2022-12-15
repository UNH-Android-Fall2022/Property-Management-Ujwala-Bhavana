package com.property.management.owner.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.databinding.FragmentChatsBinding
import java.security.MessageDigest

class ChatsFragment:Fragment()
{
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var uRecyclerView: RecyclerView
    private lateinit var chatsAdapter: ChatsAdapter
    private lateinit var userList: ArrayList<UserData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatsBinding.inflate(inflater,container,false)
        val root: View = binding.root

        userList = arrayListOf()
        db.collection("Tenants").whereEqualTo("ownerId",auth.currentUser?.uid).get()
            .addOnCompleteListener { documents ->
                for(document in documents.result){
                    val m = document.getData()
                    val name = m.get("firstName").toString() + m.get("lastName").toString()
                    val md = MessageDigest.getInstance("MD5")
                    val uid = md.digest(m.get("email").toString().trim().toByteArray(Charsets.UTF_8)).toHex()
                    userList.add(
                        UserData(name, uid )
                    )
                }
                uRecyclerView = binding.recyclerViewChats
                uRecyclerView.layoutManager = LinearLayoutManager(context)
                chatsAdapter = ChatsAdapter(userList,this)
                uRecyclerView.adapter = chatsAdapter
            }

        return root
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}