package com.example.dietdiary

import androidx.lifecycle.ViewModel

class DietListViewModel : ViewModel() {

    val dietList = mutableListOf<Diet>()

    init{
        for(i in 0 until 100){
            val diet = Diet()
            diet.weight = 58.8
            diet.water = i%3
            diet.Mood = i%5
            dietList += diet
        }
    }
}