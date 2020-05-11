package com.mygdx.guessimage.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "puzzles"
)
class PuzzleEntity : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "p_id")
    var id: Long = 0L

    @ColumnInfo(name = "p_filename")
    var filename: String? = null
}