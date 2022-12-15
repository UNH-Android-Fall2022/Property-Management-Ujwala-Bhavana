package com.property.management.owner.requests
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.property.management.R

class RequestAdapter (
    private val rlist : ArrayList<MaintenanceRequestData>,
    private val context: RequestsFragment
        ):RecyclerView.Adapter<RequestAdapter.RequestViewHolder>(){
    class RequestViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val propertyname :TextView= itemView.findViewById(R.id.property_name)
        val unitname: TextView = itemView.findViewById(R.id.unit_name)
        val subject: TextView = itemView.findViewById(R.id.issuesub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.request_card,parent,false)
        Log.d("RequestsFragment","onCreateViewHolder")
        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = rlist[position]
        holder.propertyname.text = request.propertyName
        holder.unitname.text = request.unitName
        holder.subject.text = request.subject
        Log.d("RequestsFragment","onBindViewHolder")

        holder.itemView.setOnClickListener{view ->
            Log.d("Test","Position Clicked $position")
            val action = RequestsFragmentDirections.actionNavigationRequestsToMaintenanceRequestFragment(request.propertyName,request.unitName,request.subject,request.description,request.image)
            view.findNavController().navigate(action)

        }
    }

    override fun getItemCount(): Int {
        return rlist.size
    }

}
