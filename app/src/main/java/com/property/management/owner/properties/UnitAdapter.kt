package com.property.management.owner.properties

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.property.management.GlideApp
import com.property.management.R

class UnitAdapter (
    private val uMap: HashMap<String, ArrayList<UnitData>>,
    private val context: UnitsFragment
):RecyclerView.Adapter<UnitAdapter.UnitViewHolder>(){
    class UnitViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val uimage : ImageView = itemView.findViewById(R.id.imageUnit)
        val uname : TextView = itemView.findViewById(R.id.UnitName)
        val utype : TextView = itemView.findViewById(R.id.UnitType)
        val usize : TextView = itemView.findViewById(R.id.UnitSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.added_unit,parent,false)
        return UnitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        val unit = uMap["units"]?.get(position)
        GlideApp.with(context).load(unit?.imgUrl).into(holder.uimage)
        holder.uname.text= unit?.unitName
        holder.utype.text=unit?.unitType
        holder.usize.text= unit?.unitSize.toString()+" SqFt"

        holder.itemView.setOnClickListener{view->
            Log.d("Unit Adapter","Position clicked $position")
            val propName = uMap.keys.elementAt(0)
            Log.d("UnitAdapter","$propName")
            val unitName = uMap["units"]?.get(position)!!.unitName
            Log.d("UnitAdapter","$unitName")
            val tenantId = uMap["units"]?.get(position)!!.tenantId
            val action = UnitsFragmentDirections.actionUnitsFragmentToTablayoutFragment(propName, unitName, tenantId)
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return uMap["units"]?.size!!
    }
}
