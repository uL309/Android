package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.Abilities

class AbilitiesAdapter(private val items: List<Abilities>
) : RecyclerView.Adapter<AbilitiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.abilities_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Abilities) = with(itemView) {
            val tvAbilityName = findViewById<TextView>(R.id.tvAbilitiesName)
            tvAbilityName.text = item.getAbilityName()
            val tvAbilityHidden = findViewById<TextView>(R.id.tvAbilityHidden)
            if (item.is_hidden) {
                tvAbilityHidden.text = "Hidden"
            } else {
                tvAbilityHidden.visibility = View.GONE
            }
            val tvAbilitySlot = findViewById<TextView>(R.id.tvAbilitySlot)
            tvAbilitySlot.text = item.slot.toString()
        }
    }
}
