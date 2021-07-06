package com.example.dietdiary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.dietdiary.Diet


@Database(entities = [Diet::class], version = 1)
@TypeConverters(DietTypeConverters::class)
abstract class DietDatabase : RoomDatabase() {

    abstract fun dietDao() : DietDao
}