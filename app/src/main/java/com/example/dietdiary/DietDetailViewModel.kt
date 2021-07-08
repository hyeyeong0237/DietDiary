package com.example.dietdiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class DietDetailViewModel() : ViewModel(){

    private val dietRepository = DietRepository.get()
    private val dietIdLiveData = MutableLiveData<UUID>()

    var dietLiveData : LiveData<Diet?> =
        Transformations.switchMap(dietIdLiveData) { dietId ->
            dietRepository.getDiet(dietId)
        }

    fun loadDiet(dietId: UUID){
        dietIdLiveData.value = dietId
    }
    fun getPhotoFile(diet: Diet) : File {
        return dietRepository.getPhotoFile(diet)
    }
    fun saveDiet(diet: Diet){
        dietRepository.updateDiet(diet)
    }
    fun deleteDiet(diet: Diet){
        dietRepository.deleteDiet(diet)
    }
}