package com.example.myinformatika

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class InfoAdapter(
    private val infoList: List<InfoItem>,
    private val onDeleteClick: (InfoItem) -> Unit
) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val descText: TextView = itemView.findViewById(R.id.descText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val viewsText: TextView = itemView.findViewById(R.id.viewsText)

        // File section views (nullable since they might not exist in both layouts)
        val layoutFileInfo: LinearLayout? = itemView.findViewById(R.id.layoutFileInfo)
        val fileName: TextView? = itemView.findViewById(R.id.file_name)
        val fileSize: TextView? = itemView.findViewById(R.id.file_size)
//        val btnDownload: Button? = itemView.findViewById(R.id.btnDownload)
        val btnDownload: TextView? = itemView.findViewById(R.id.btnDownload)

        val deleteText: TextView? = itemView.findViewById(R.id.deleteText)
    }

    override fun getItemViewType(position: Int): Int {
        return if (!infoList[position].fileName.isNullOrEmpty()) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutRes = if (viewType == 1) R.layout.info_item else R.layout.info_item_text
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val currentItem = infoList[position]

        // Common views
        holder.titleText.text = currentItem.title
        holder.descText.text = currentItem.description
        holder.dateText.text = currentItem.date
        holder.viewsText.text = "${currentItem.views} views"

        // File info
        if (getItemViewType(position) == 1) {
            holder.layoutFileInfo?.visibility = View.VISIBLE
            holder.fileName?.text = currentItem.fileName
            holder.fileSize?.text = currentItem.fileSize
            holder.btnDownload?.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Mengunduh ${currentItem.fileName}", Toast.LENGTH_SHORT).show()

                // TODO: Implementasi download nyata, misalnya pakai DownloadManager jika file dari URL
            }

        } else {
            holder.layoutFileInfo?.visibility = View.GONE
        }

        // Tombol Delete
        holder.deleteText?.setOnClickListener {
            onDeleteClick(currentItem)
        }
    }

    override fun getItemCount() = infoList.size
}