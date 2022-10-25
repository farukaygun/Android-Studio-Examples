package com.farukaygun.kotlinartbookwithfragments.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farukaygun.kotlinartbookwithfragments.model.Art

@Database(entities = arrayOf(Art::class), version = 1)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao() : ArtDao
}