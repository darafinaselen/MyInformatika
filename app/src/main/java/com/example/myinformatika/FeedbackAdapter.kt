package com.example.myinformatika

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

open class FeedbackAdapter(
    options: FirestoreRecyclerOptions<FeedbackItem>,
    private val context: Context
) : FirestoreRecyclerAdapter<FeedbackItem, FeedbackAdapter.FeedbackViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feedback_admin, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int, model: FeedbackItem) {
        holder.bind(model)
    }

    inner class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val detailBtn: MaterialButton = itemView.findViewById(R.id.detailBtn)
        private val deleteBtn: MaterialButton = itemView.findViewById(R.id.deleteBtn)
        private val db = FirebaseFirestore.getInstance()

        fun bind(feedback: FeedbackItem) {
            tvCategory.text = feedback.category
            feedback.timestamp?.toDate()?.let { date ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                tvDate.text = sdf.format(date)
            } ?: run {
                tvDate.text = feedback.date
            }
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = feedback.isDone

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (feedback.id.isNotEmpty()) {
                    db.collection("feedback").document(feedback.id)
                        .update("isDone", isChecked)
                        .addOnFailureListener {
                            Toast.makeText(context, "Gagal update status", Toast.LENGTH_SHORT).show()
                            checkBox.isChecked = !isChecked
                        }
                }
            }

            detailBtn.setOnClickListener { showDetailDialog(feedback) }
            deleteBtn.setOnClickListener { showDeleteConfirmationDialog(feedback) }
        }

        private fun showDetailDialog(feedback: FeedbackItem) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_feedback_detail, null)
            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val tvDetailDate: TextView = dialogView.findViewById(R.id.tvDetailDate)
            val tvDetailCategory: TextView = dialogView.findViewById(R.id.tvDetailCategory)
            val tvDetailContent: TextView = dialogView.findViewById(R.id.tvDetailContent)
            val ivCloseDialog: ImageView = dialogView.findViewById(R.id.ivCloseDialog)

            feedback.timestamp?.toDate()?.let { date ->
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                tvDetailDate.text = sdf.format(date)
            } ?: run {
                tvDetailDate.text = feedback.date
            }

            tvDetailCategory.text = feedback.category
            tvDetailContent.text = feedback.feedbackContent

            ivCloseDialog.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }

        private fun showDeleteConfirmationDialog(feedback: FeedbackItem) {
            if (feedback.id.isEmpty()) {
                Toast.makeText(context, "ID Dokumen tidak ditemukan, tidak bisa menghapus.", Toast.LENGTH_SHORT).show()
                return
            }
            AlertDialog.Builder(context)
                .setTitle("Hapus Feedback")
                .setMessage("Apakah Anda yakin ingin menghapus feedback ini?")
                .setPositiveButton("Hapus") { _, _ ->
                    db.collection("feedback").document(feedback.id).delete()
                        .addOnSuccessListener { Toast.makeText(context, "Feedback dihapus", Toast.LENGTH_SHORT).show() }
                        .addOnFailureListener { Toast.makeText(context, "Gagal menghapus", Toast.LENGTH_SHORT).show() }
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }
}