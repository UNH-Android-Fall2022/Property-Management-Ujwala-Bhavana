package com.property.management.owner.properties.payments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.property.management.R

class PaymentAdapter (
    private val paymentList : ArrayList<PaymentData>,
    private val context : PaymentsFragment
        ): RecyclerView.Adapter<PaymentAdapter.paymentViewHolder>(){
            class paymentViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
            {
                val amount:TextView = itemView.findViewById(R.id.textView3)
                val rentForMonth:TextView = itemView.findViewById(R.id.textView2)
                val datePaid:TextView = itemView.findViewById(R.id.textView1)
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): paymentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_payment,parent,false)
        return paymentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: paymentViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.amount.text = payment.amount.toString()
        holder.datePaid.text = payment.datePaid
        holder.rentForMonth.text = payment.paymentForMonth
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }
}
