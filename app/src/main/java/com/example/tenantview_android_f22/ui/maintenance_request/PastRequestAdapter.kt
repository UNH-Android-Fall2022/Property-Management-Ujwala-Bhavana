package com.example.tenantview_android_f22.ui.maintenance_request

import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import com.example.tenantview_android_f22.R


class PastRequestAdapter(
    private val mExampleList: ArrayList<PastRequestCard>,
    private val context: MaintenanceRequestFragment
) : RecyclerView.Adapter<PastRequestAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
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
        val ( subject , description ) = mExampleList[position]
        holder.subject.text = subject
        holder.description.text = description
        holder.itemView.setOnClickListener{
            Log.d(TAG,"Position Clicked: $position")
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int{
        return mExampleList.size
    }

}
