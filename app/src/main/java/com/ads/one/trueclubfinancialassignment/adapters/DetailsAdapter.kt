package com.ads.one.trueclubfinancialassignment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ads.one.trueclubfinancialassignment.R
import com.ads.one.trueclubfinancialassignment.modals.DetailsModal

//adapter class for showing recycler view
class DetailsAdapter(val context: Context, var detailsList: ArrayList<DetailsModal>) :
    RecyclerView.Adapter<DetailsAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val country: TextView = itemView.findViewById(R.id.country)
        val probability: TextView = itemView.findViewById(R.id.probability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_details_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.country.text = "Country :- " + detailsList[position].country
        holder.probability.text = "Probability :- " + detailsList[position].probability
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    fun updateData(detailsLists: ArrayList<DetailsModal>) {
        detailsList = detailsLists
        notifyDataSetChanged()
    }
}