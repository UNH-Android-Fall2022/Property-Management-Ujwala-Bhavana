package com.example.property_management

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class UnitAdapter (
    private val ulist:ArrayList<UnitData>,
    private val context:UnitsFragment
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
        val unit = ulist[position]
        holder.uname.text=unit.unitName
        holder.utype.text=unit.unitType
        holder.usize.text= unit.unitSize.toString()

        holder.itemView.setOnClickListener{
            Log.d("Test","Position clicked $position")
        }
    }

    override fun getItemCount(): Int {
        return ulist.size
    }
}
