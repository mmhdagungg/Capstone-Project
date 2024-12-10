package com.kamalapp.cashify.ui.artikel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.artikel.Artikel

class ListArtikelAdapter(private val artikelList: ArrayList<Artikel>) :
    RecyclerView.Adapter<ListArtikelAdapter.ArtikelViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_articles, parent, false)
        return ArtikelViewHolder(view)
    }

    override fun getItemCount(): Int = artikelList.size

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val artikel = artikelList[position]
        holder.imgPhoto.setImageResource(artikel.gambar)
        holder.tvName.text = artikel.judul
        holder.tvDescription.text = artikel.deskripsi


        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(artikelList[holder.adapterPosition])
        }
    }

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.ivItemPhoto)
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvItemDescription)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Artikel)
    }
}
