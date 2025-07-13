package com.example.myinformatika

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class DynamicInfoAdapter(private val infoList: List<InfoItem>) :
    RecyclerView.Adapter<DynamicInfoAdapter.InfoViewHolder>() {

    // Konstanta untuk tipe view
    companion object {
        private const val TIPE_UTAMA = 1
        private const val TIPE_SEKUNDER = 2
    }

    class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Deklarasikan semua view dari kedua layout
        val layoutUtama: LinearLayout = view.findViewById(R.id.layout_item_utama)
        val titleUtama: TextView = view.findViewById(R.id.tvInfoTitleUtama)
        val dateUtama: TextView = view.findViewById(R.id.tvInfoDateUtama)

        val layoutSekunder: ConstraintLayout = view.findViewById(R.id.layout_item_sekunder)
        val imageSekunder: ImageView = view.findViewById(R.id.ivInfoImage)
        val titleSekunder: TextView = view.findViewById(R.id.tvInfoTitle)
        val dateSekunder: TextView = view.findViewById(R.id.tvInfoDate)
    }

    override fun getItemViewType(position: Int): Int {
        // Gunakan viewType dari data class
        return infoList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        // Selalu gunakan layout item_info_vertical.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_info_vertical, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val infoItem = infoList[position]

        // Cek tipe view dan tampilkan layout yang sesuai
        when (holder.itemViewType) {
            TIPE_UTAMA -> {
                holder.layoutUtama.visibility = View.VISIBLE
                holder.layoutSekunder.visibility = View.GONE
                holder.titleUtama.text = infoItem.title
                holder.dateUtama.text = infoItem.date
            }
            TIPE_SEKUNDER -> {
                holder.layoutUtama.visibility = View.GONE
                holder.layoutSekunder.visibility = View.VISIBLE
                holder.titleSekunder.text = infoItem.title
                holder.dateSekunder.text = infoItem.date
                holder.imageSekunder.setImageResource(infoItem.image)
            }
        }
    }

    override fun getItemCount() = infoList.size
}