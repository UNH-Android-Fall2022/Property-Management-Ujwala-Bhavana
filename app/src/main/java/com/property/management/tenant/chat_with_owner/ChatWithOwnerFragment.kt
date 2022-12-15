package com.property.management.tenant.chat_with_owner

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.property.management.databinding.FragmentChatWithOwnerBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.owner.chats.ChatAdapter
import com.property.management.owner.chats.MessageData
import java.security.MessageDigest
import java.util.ArrayList


class ChatWithOwnerFragment : Fragment() {

    private var _binding: FragmentChatWithOwnerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val TAG = "Property_Management"
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val aut: ChatWithOwnerFragmentArgs by navArgs()

    lateinit var senderRoom:String
    lateinit var receiverRoom: String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var chatAdapter: chatAdapter
    private lateinit var messageList: ArrayList<MessageData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatWithOwnerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val md = MessageDigest.getInstance("MD5")
        val uid = md.digest(auth.currentUser?.email.toString().trim().toByteArray(Charsets.UTF_8)).toHex()

        lateinit var receiverId: String
        val senderuid = uid

        receiverId = aut.ownerId
        binding.txtreceiverNameTenant.text = aut.name

        senderRoom = receiverId + senderuid
        receiverRoom = senderuid + receiverId

        messageList = arrayListOf()
        mRecyclerView = binding.recyclerViewMessagesTenant
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        chatAdapter = chatAdapter(messageList,this)
        mRecyclerView.adapter = chatAdapter

        //Logic for adding messages to arraylist
        db.collection("chats").document(senderRoom).collection("messages").orderBy("timestamp")
            .addSnapshotListener{snapshot , e ->
                if(e!=null){
                    Log.d("ChatFragmentTenant","Error while loading messages from Firestore")
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    messageList.clear()
                    for(messageDocument in snapshot.documents){
                        val m = messageDocument.getData()
                        messageList.add(MessageData(m?.get("message").toString(),m?.get("senderId").toString(),null))
                    }
                    chatAdapter.notifyDataSetChanged()
                }
            }



        binding.buttonSendTenant.setOnClickListener{
            val message = binding.edittextChatTenant.text.toString()
            val calendar = Calendar.getInstance()
            val currentTime = calendar.time
            val messageData = MessageData(message, senderuid!!, currentTime)
            val msg = hashMapOf(
                "message" to messageData.message,
                "senderId" to messageData.senderId,
                "timestamp" to messageData.timestamp
            )
            db.collection("chats").document(senderRoom).collection("messages").add(msg)
                .addOnSuccessListener {
                    db.collection("chats").document(receiverRoom).collection("messages").add(msg)
                }
            binding.edittextChatTenant.setText("")
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}