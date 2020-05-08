package com.mygdx.guessimage.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded

class PuzzleCount {

    @Embedded
    lateinit var puzzle: PuzzleEntity

    @ColumnInfo(name = "objects", defaultValue = "0")
    var objects = 0
}