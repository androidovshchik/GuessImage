package com.mygdx.guessimage.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mygdx.guessimage.local.dao.CommonDao
import com.mygdx.guessimage.local.dao.ObjectDao
import com.mygdx.guessimage.local.dao.PuzzleDao
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.local.entities.PuzzleEntity

@Database(
    entities = [
        PuzzleEntity::class,
        ObjectEntity::class
    ],
    version = 3
)
abstract class Database : RoomDatabase() {

    abstract fun commonDao(): CommonDao

    abstract fun puzzleDao(): PuzzleDao

    abstract fun objectDao(): ObjectDao
}