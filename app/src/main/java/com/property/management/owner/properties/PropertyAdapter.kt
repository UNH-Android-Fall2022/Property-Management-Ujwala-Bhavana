package com.property.management.owner.properties

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.property.management.R
import com.property.management.GlideApp

class PropertyAdapter (
    private val plist : ArrayList<PropertyData>,
    private val context : PropertiesFragment
):RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>(){
    class PropertyViewHolder(itemView: android.view.View):RecyclerView.ViewHolder(itemView){
        val pImageView:ImageView = itemView.findViewById(R.id.imageView)
        val pName: TextView = itemView.findViewById(R.id.txtName)
        val pUnits: TextView = itemView.findViewById(R.id.txtunits)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.added_property,parent,false)
        Log.d("Test","view holder created")
        return PropertyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        Log.d("Test", "on bindviewholder")
        val property = plist[position]
        GlideApp.with(context).load(property.imgUrl).into(holder.pImageView)
        holder.pName.text = property.propertyName
        holder.pUnits.text = property.units.toString() + " Units"

        holder.itemView.setOnClickListener{view ->
            Log.d("Test","Position Clicked $position")
            Log.d("Test", "${plist[position].propertyName}")
            val name:String = plist[position].propertyName
            val action = PropertiesFragmentDirections.actionNavigationPropertiesToUnitsFragment(name)
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return plist.size
    }
}