package com.andrei.cerbulescu.placestracker.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "place_table")
data class Place (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val image: ByteArray = ByteArray(0)
    ): Parcelable {

}