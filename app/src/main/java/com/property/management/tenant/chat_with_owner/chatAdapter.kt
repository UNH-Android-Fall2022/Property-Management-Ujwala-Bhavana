package com.property.management.tenant.chat_with_owner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.property.management.R
import com.property.management.owner.chats.ChatAdapter
import com.property.management.owner.chats.MessageData
import java.security.MessageDigest

class chatAdapter(val messageList: ArrayList<MessageData>, val context: ChatWithOwnerFragment):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val Item_Sent = 2
    val Item_Receive = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            //inflate receive
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messagereceived_tenant,parent,false)
            return ReceiveViewHolder(itemView)
        }
        else{
            //inflate sent
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messagesent_tenant,parent,false)
            return SentViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = message.message
        }
        else{
            val viewHolder = holder as ReceiveViewHolder
            holder.receivedMessage.text = message.message
        }
    }
    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        val md = MessageDigest.getInstance("MD5")
        val uid = md.digest(FirebaseAuth.getInstance().currentUser?.email.toString().trim().toByteArray(Charsets.UTF_8)).toHex()
        if(uid.equals(message.senderId))
            return Item_Sent
        else
            return Item_Receive
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById(R.id.txtsentmsg_tenant)
    }
    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receivedMessage: TextView = itemView.findViewById(R.id.txtreceivemsg_tenant)
    }
    private fun ByteArray.toHex() :String = joinToString(separator = "") { byte -> "%02x".format(byte) }
}