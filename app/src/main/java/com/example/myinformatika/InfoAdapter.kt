package com.example.myinformatika

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class InfoAdapter(private val items: List<InfoItem>) :
    RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView = itemView.findViewById(R.id.infoCategory)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val descText: TextView = itemView.findViewById(R.id.descText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val viewsText: TextView = itemView.findViewById(R.id.viewsText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.info_item, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = items[position]
        holder.categoryText.text = item.category
        holder.titleText.text = item.title
        holder.descText.text = item.description
        holder.dateText.text = item.date
        holder.viewsText.text = item.views
    }

    override fun getItemCount(): Int = items.size
}
