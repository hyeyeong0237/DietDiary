package com.example.dietdiary

import java.util.*

data class Diet(
    val id: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var weight: Double = 0.0,
    var water : Int = 0,
    var Meal : String = "",
    var Exercise: String = "",
    var Mood : Int = 0
) {
}