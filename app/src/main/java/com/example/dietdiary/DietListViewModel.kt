package com.example.dietdiary

import androidx.lifecycle.ViewModel

class DietListViewModel : ViewModel() {

    private val dietRepository = DietRepository.get()
    val dietListLiveData = dietRepository.getDiets()

    fun addDiet(diet: Diet){
        dietRepository.addDiet(diet)
    }
}