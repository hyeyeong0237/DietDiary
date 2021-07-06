package com.example.dietdiary

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.dietdiary.database.DietDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "diet-database"

class DietRepository private constructor(context: Context){


    private val database : DietDatabase = Room.databaseBuilder(
        context.applicationContext,
        DietDatabase::class.java,
        DATABASE_NAME
    ).build()


    private val dietDao = database.dietDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getDiets() : LiveData<List<Diet>> = dietDao.getDiets()
    fun getDiet(id: UUID) : LiveData<Diet?> = dietDao.getDiet(id)

    fun updateDiet(diet: Diet){
        executor.execute{
            dietDao.updateDiet(diet)
        }
    }
    fun addDiet(diet: Diet){
        executor.execute{
            dietDao.addDiet(diet)
        }
    }
    fun deleteDiet(diet: Diet) {
        executor.execute {
            dietDao.deleteDiet(diet)
        }
    }



    companion object {
        private var INSTANCE : DietRepository? = null


        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = DietRepository(context)
            }
        }


        fun get() : DietRepository {
            return INSTANCE ?:
            throw IllegalStateException("DietRepository must be initialized")
        }

    }
}