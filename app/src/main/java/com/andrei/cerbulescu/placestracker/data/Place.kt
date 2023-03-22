package com.andrei.cerbulescu.placestracker.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "place_table")
data class Place (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val image: ByteArray
    ): Parcelable