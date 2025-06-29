package com.example.myinformatika

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterRecycleView (
    private val items: List<FeedbackItem>,
    private val onDetailClick: (FeedbackItem) -> Unit,
    private val onDeleteClick: (FeedbackItem) -> Unit,
    private val onCheckboxChecked: ((FeedbackItem, Boolean) -> Unit)? = null
) : RecyclerView.Adapter<AdapterRecycleView.FeedbackViewHolder>() {

    class FeedbackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val year: TextView = view.findViewById(R.id.feedbackYear)
        val category: TextView = view.findViewById(R.id.feedbackCategory)
        val detailBtn: Button = view.findViewById(R.id.detailBtn)
        val deleteBtn: Button = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback_admin, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val item = items[position]
        holder.year.text = item.year
        holder.category.text = item.category

        // Mengatur checkbox
        holder.checkBox.setOnCheckedChangeListener(null)
        // holder.checkBox.isChecked = item.isChecked // Jika Anda memiliki properti isChecked di FeedbackItem
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxChecked?.invoke(item, isChecked)
        }

        holder.detailBtn.setOnClickListener { onDetailClick(item) }

        if (item.showDelete) {
            holder.deleteBtn.visibility = View.VISIBLE
            holder.deleteBtn.setOnClickListener { onDeleteClick(item) }
        } else {
            holder.deleteBtn.visibility = View.GONE
        }
    }
}