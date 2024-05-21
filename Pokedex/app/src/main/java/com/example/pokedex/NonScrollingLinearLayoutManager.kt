package com.example.pokedex

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NonScrollingLinearLayoutManager (context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return false
    }
}