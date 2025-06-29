// File: HomeFeedbackAdapter.kt
package com.example.myinformatika

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class HomeFeedbackAdapter(private val feedbackList: List<FeedbackItem>) :
    RecyclerView.Adapter<HomeFeedbackAdapter.FeedbackViewHolder>() {

    class FeedbackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.tvFeedbackContent)
        val category: TextView = view.findViewById(R.id.tvFeedbackCategory)
        val date: TextView = view.findViewById(R.id.tvFeedbackDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedbackItem = feedbackList[position]

        holder.content.text = feedbackItem.feedbackContent
        holder.category.text = feedbackItem.category

        // Format tanggal agar mudah dibaca
        feedbackItem.timestamp?.toDate()?.let {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            holder.date.text = sdf.format(it)
        } ?: run {
            holder.date.text = "Tanggal tidak tersedia"
        }
    }

    override fun getItemCount() = feedbackList.size
}