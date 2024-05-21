package com.example.pokedex

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.database.PokeData


class PokemonAdapter(
    private val items: List<PokeData?>
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bindView(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: PokeData?) = with(itemView) {
            //val ivPokemon = findViewById<ImageView>(R.id.ivPokemon)
            val tvNumber = findViewById<TextView>(R.id.tvNumber)
            val tvName = findViewById<TextView>(R.id.tvName)
            val tvType1 = findViewById<TextView>(R.id.tvType1)
            val tvType2 = findViewById<TextView>(R.id.tvType2)

            item?.let {
                //Glide.with(itemView.context).load(it.imageUrl).into(ivPokemon)

                tvNumber.text = "Nº ${item.id}"
                tvName.text = item.name.replaceFirstChar { it.uppercase() }
                tvType1.text = item.type[0].name.replaceFirstChar { it.uppercase() }

                if (item.type.size > 1) {
                    tvType2.visibility = View.VISIBLE
                    tvType2.text = item.type[1].name.replaceFirstChar { it.uppercase() }
                } else {
                    tvType2.visibility = View.GONE
                }
                setOnClickListener {
                    val intent = Intent(context, sumary::class.java)
                    intent.putExtra("number", item.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}