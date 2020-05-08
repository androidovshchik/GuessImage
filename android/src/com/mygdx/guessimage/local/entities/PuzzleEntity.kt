package com.mygdx.guessimage.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "puzzles"
)
class PuzzleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "p_id")
    var id: Long? = null,
    @ColumnInfo(name = "p_path")
    var path: String
)