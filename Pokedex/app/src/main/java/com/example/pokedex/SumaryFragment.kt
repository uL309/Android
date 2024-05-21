package com.example.pokedex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class SumaryFragment : Fragment() {
    private lateinit var viewModel: SumaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_sumary, container, false)
        viewModel = ViewModelProvider(requireActivity())[SumaryViewModel::class.java]
        // Inflate the layout for this fragment
        viewModel.pokemon.observe(viewLifecycleOwner) { pokemon ->
            val number = view.findViewById<TextView>(R.id.Number)
            val name = view.findViewById<TextView>(R.id.Name)
            val height = view.findViewById<TextView>(R.id.height)
            val weight = view.findViewById<TextView>(R.id.weight)
            val base_experience = view.findViewById<TextView>(R.id.BaseExps)
            val type1 = view.findViewById<TextView>(R.id.tvType1)
            val type2 = view.findViewById<TextView>(R.id.tvType2)
            val rvAbilities = view.findViewById<RecyclerView>(R.id.rvAbilities)
            val rvMoves = view.findViewById<RecyclerView>(R.id.rvMoves)
            val rvStats = view.findViewById<RecyclerView>(R.id.rvStats)
            val abilitiesAdapter = AbilitiesAdapter(pokemon.abilities)
            val movesAdapter = MovesAdapter(pokemon.moves)
            val statsAdapter = StatsAdapter(pokemon.stats)

            number.text = pokemon.id.toString()
            name.text = pokemon.name
            height.text = pokemon.height.toString()
            weight.text = pokemon.weight.toString()
            base_experience.text = pokemon.base_experience.toString()
            type1.text = pokemon.types[0].type.name
            if (pokemon.types.size > 1) {
                type2.visibility = View.VISIBLE
                type2.text = pokemon.types[1].type.name
            } else {
                type2.visibility = View.GONE
            }
            rvAbilities.layoutManager = context?.let { NonScrollingLinearLayoutManager(it) }
            rvAbilities.adapter = abilitiesAdapter

            rvMoves.layoutManager = context?.let { NonScrollingLinearLayoutManager(it) }
            rvMoves.adapter = movesAdapter

            rvStats.layoutManager = context?.let { NonScrollingLinearLayoutManager(it) }
            rvStats.adapter = statsAdapter

        }
        return view
    }

}
