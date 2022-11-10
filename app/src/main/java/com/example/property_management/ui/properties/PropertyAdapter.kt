package com.example.property_management.ui.properties

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.property_management.PropertyData
import com.example.property_management.R
import com.google.firebase.firestore.core.View

class PropertyAdapter (
    private val plist : ArrayList<PropertyData>,
            private val context : PropertiesFragment
):RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>(){
    class PropertyViewHolder(itemView: android.view.View):RecyclerView.ViewHolder(itemView){
        // val pImageView:ImageView = itemView.findViewById(R.id.imageView)
        val pName: TextView = itemView.findViewById(R.id.txtName)
        val pUnits: TextView = itemView.findViewById(R.id.txtUnits)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.added_property,parent,false)
        Log.d("Test","view holder created")
        return PropertyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        Log.d("Test", "on bindviewholder")
        val property = plist[position]
        //TODO ("Implement Image")
        holder.pName.text = property.propertyName
        holder.pUnits.text = property.units
        Log.d("Test","Binds values to Listitems")
        holder.itemView.setOnClickListener{
            Log.d("Test","Position Clicked $position")

        }
    }

    override fun getItemCount(): Int {
        return plist.size
    }
}