package com.property.management.owner.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.property.management.R

class ChatsAdapter(val userlist:ArrayList<UserData>, val context: ChatsFragment):RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    class ChatsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val uname:TextView = itemView.findViewById(R.id.chat_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_card, parent, false)
        return ChatsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val user = userlist[position]
        holder.uname.text = user.name

        holder.itemView.setOnClickListener{view ->
            val action =
                ChatsFragmentDirections.actionChatsFragmentToChatFragment(user.uid, user.name)
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userlist.size
    }
}