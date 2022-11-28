package com.example.tenantview_android_f22.ui.maintenance_request

import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.example.tenantview_android_f22.R



class PastRequestAdapter(
    private val mExampleList: ArrayList<PastRequestData>,
    private val context: MaintenanceRequestFragment
) : RecyclerView.Adapter<PastRequestAdapter.ViewHolder>() {
    private val TAG = "Property_Management"
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.requestSubject)
        val description : TextView = itemView.findViewById(R.id.requestDescription)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.past_request_items_list, parent, false)
        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view with that element
        //val ( subject , description ) = mExampleList[position]
        val req = mExampleList[position]
        Log.d(TAG,"IN ADAPTERB SUB: ${req.d_subject}")
        holder.subject.text = req.d_subject
        holder.description.text = req.d_description
        holder.itemView.setOnClickListener{view->
            Log.d(TAG,"Position Clicked: $position")
            val action = MaintenanceRequestFragmentDirections.actionNavigationMaintenanceRequestToViewPastRequestFragment(req.d_subject,req.d_description)
            view.findNavController().navigate(action)
            //Navigation
            //    .createNavigateOnClickListener(R.id.action_navigation_maintenance_request_to_viewPastRequestFragment,bundle).onClick(holder.itemView)


        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int{
        return mExampleList.size
    }

}


