package com.example.pokedex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.PokeList

class SumaryViewModel: ViewModel(){
    val pokemon = MutableLiveData<PokeList>()
}