package com.property.management.owner.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.property.management.R

class ChatAdapter(val messageList:ArrayList<MessageData>, val context: ChatFragment):RecyclerView.Adapter<RecyclerView  .ViewHolder>(){
    val Item_Sent = 2
    val Item_Receive = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            //inflate receive
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messagereceived,parent,false)
            return ReceiveViewHolder(itemView)
        }
        else{
            //inflate sent
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messagesent,parent,false)
            return SentViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentmessage.text = message.message
        }
        else{
            val viewHolder = holder as ReceiveViewHolder
            holder.receivemessage.text = message.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(message.senderId))
            return Item_Sent
        else
            return Item_Receive
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentmessage: TextView = itemView.findViewById(R.id.txtsentmsg)
    }
    class ReceiveViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val receivemessage: TextView = itemView.findViewById(R.id.txtreceivemsg)
    }
}
