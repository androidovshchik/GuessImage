package com.mygdx.guessimage.local.entities

import androidx.room.*
import java.io.Serializable

@Entity(
    tableName = "objects",
    foreignKeys = [
        ForeignKey(
            entity = PuzzleEntity::class,
            parentColumns = ["p_id"],
            childColumns = ["o_p_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["o_p_id"])
    ]
)
class ObjectEntity : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "o_id")
    var id = 0L

    @ColumnInfo(name = "o_p_id")
    var puzzleId = 0L

    @ColumnInfo(name = "o_name")
    var name: String? = null
}