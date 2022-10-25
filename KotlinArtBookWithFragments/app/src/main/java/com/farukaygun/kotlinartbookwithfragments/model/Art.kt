package com.farukaygun.kotlinartbookwithfragments.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Art(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "artistname")
    var artist: String,

    @ColumnInfo(name = "year")
    var year: String,

    @ColumnInfo(name = "image")
    var image : ByteArray?
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}