package com.nasch.househero.ServicesRecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nasch.househero.R
import com.nasch.househero.SealedClasses.Services

class ServicesAdapter(private val categories: List<Services>, private val onItemSelected: (Int) -> Unit) :
    RecyclerView.Adapter<ServicesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false)
        return ServicesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.render(categories[position], onItemSelected)

    }

    override fun getItemCount() =  categories.size
}