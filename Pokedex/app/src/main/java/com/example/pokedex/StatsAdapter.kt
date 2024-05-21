package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.Stats

class StatsAdapter(private val items: List<Stats>) : RecyclerView.Adapter<StatsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.stats_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Stats) = with(itemView) {
            val tvStatName = findViewById<TextView>(R.id.tvStatName)
            tvStatName.text = item.getStatName()
            val tvStatValue = findViewById<TextView>(R.id.tvBaseStat)
            tvStatValue.text = item.base_stat.toString()
            val tvStatEffort = findViewById<TextView>(R.id.tvEffort)
            tvStatEffort.text = item.effort.toString()


        }
    }
}
