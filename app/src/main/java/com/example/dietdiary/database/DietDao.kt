package com.example.dietdiary.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dietdiary.Diet
import java.util.*


@Dao
interface DietDao {

    @Query("SELECT * FROM diet")
    fun getDiets(): LiveData<List<Diet>>

    @Query("SELECT * FROM diet WHERE id = (:id)")
    fun getDiet(id : UUID) : LiveData<Diet?>

    @Update
    fun updateDiet(diet: Diet)

    @Insert
    fun addDiet(diet: Diet)

    @Delete
    fun deleteDiet(diet: Diet)

}