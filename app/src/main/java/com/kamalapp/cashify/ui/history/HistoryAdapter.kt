package com.kamalapp.cashify.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kamalapp.cashify.R
import com.kamalapp.cashify.data.HistoryItem

class HistoryAdapter(
    private var items: List<HistoryItem>,
    private val onItemClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val icon: ImageView = itemView.findViewById(R.id.iv_icon)

        fun bind(item: HistoryItem) {
            title.text = item.title
            date.text = item.date
            icon.setImageResource(item.iconRes)
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HistoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
