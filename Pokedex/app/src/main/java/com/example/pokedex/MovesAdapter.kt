package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.pokedex.data.Moves

class MovesAdapter(private val items: List<Moves>) : RecyclerView.Adapter<MovesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.moves_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Moves) = with(itemView) {
            val tvMoveName = findViewById<TextView>(R.id.tvAbilitiesName)
            tvMoveName.text = item.getMoveName()
            val tvLevelLearnedAt = findViewById<TextView>(R.id.tvMoveLevel)
            tvLevelLearnedAt.text = item.getLevelLearnedAt().toString()
            val tvMoveLearnMethod = findViewById<TextView>(R.id.tvLearnMethod)
            tvMoveLearnMethod.text = item.getMoveLearnMethod()
            val tvVersionGroup = findViewById<TextView>(R.id.tvVersion)
            tvVersionGroup.text = item.getVersionGroup()
        }
    }

}
