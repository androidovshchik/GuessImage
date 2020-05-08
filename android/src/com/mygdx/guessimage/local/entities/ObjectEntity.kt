package com.mygdx.guessimage.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "objects"
)
class ObjectEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "o_id")
    var id: Long? = null,
    @ColumnInfo(name = "o_path")
    var path: String,
    @ColumnInfo(name = "o_name")
    var name: String
)