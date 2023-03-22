package com.andrei.cerbulescu.placestracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_table")
data class Place (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val image: ByteArray
    )