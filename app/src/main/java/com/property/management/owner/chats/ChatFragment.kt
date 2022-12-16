package com.property.management.owner.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.property.management.databinding.FragmentChatBinding
import java.util.*


class ChatFragment:Fragment()
{
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val aut: com.property.management.owner.chats.ChatFragmentArgs by navArgs()

    private lateinit var receivername:String
    private lateinit var receiveruid: String
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageList: ArrayList<MessageData>

    lateinit var senderRoom:String
    lateinit var receiverRoom: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater,container,false)
        val root: View = binding.root

        receivername = aut.name
        receiveruid = aut.uid

        binding.txtsenderName.text = receivername
        val senderuid = auth.currentUser!!.uid

        senderRoom = receiveruid + senderuid
        receiverRoom = senderuid + receiveruid

        messageList = arrayListOf()
        mRecyclerView = binding.recyclerViewMessages
        mRecyclerView.layoutManager=LinearLayoutManager(context)
        chatAdapter = ChatAdapter(messageList,this)
        mRecyclerView.adapter = chatAdapter

        //Logic for adding data to recyclerview
        db.collection("chats").document(senderRoom).collection("messages").orderBy("timestamp")
            .addSnapshotListener{snapshot, e ->
                if(e!=null){
                    Log.d("ChatFragment","Error while loading messages from Firestore")
                    return@addSnapshotListener
                }

                if(snapshot != null){
                    messageList.clear()
                    for(messageDocument in snapshot.documents){
                        val m = messageDocument.getData()
                        messageList.add(
                            MessageData(
                            m?.get("message").toString(),
                            m?.get("senderId").toString(),
                            null
                        )
                        )
                    }
                    chatAdapter.notifyDataSetChanged()
                }

            }
            //Adding the message to Database when click on send button
            binding.buttonSend.setOnClickListener{
            val message = binding.edittextChat.text.toString()
            val calendar = android.icu.util.Calendar.getInstance()
            val currenttime = calendar.time
            val messageData = MessageData(message,senderuid, currenttime)
                val msg = hashMapOf(
                    "message" to messageData.message,
                    "senderId" to messageData.senderId,
                    "timestamp" to messageData.timestamp
                )
                Log.d("Chat","check")
            db.collection("chats").document(senderRoom).collection("messages").add(msg)
                .addOnSuccessListener {
                    db.collection("chats").document(receiverRoom).collection("messages").add(msg)
                }
            binding.edittextChat.setText("")


        }


        return root

    }

}