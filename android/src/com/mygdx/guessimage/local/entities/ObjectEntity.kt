package com.mygdx.guessimage.local.entities

import androidx.room.*

@Entity(
    tableName = "objects",
    foreignKeys = [
        ForeignKey(
            entity = PuzzleEntity::class,
            parentColumns = ["p_id"],
            childColumns = ["o_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["o_id"])
    ]
)
class ObjectEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "o_id")
    var id: Long = 0L,
    @ColumnInfo(name = "o_path")
    var path: String,
    @ColumnInfo(name = "o_name")
    var name: String
)