package com.example.dietdiary

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Diet(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var weight: Double = 0.0,
    var water : Int = 0,
    var Meal : String = "",
    var Exercise: String = "",
    var Mood : Int = 0
) {
    val photoFileName get() = "IMG_$id.jpg"
}