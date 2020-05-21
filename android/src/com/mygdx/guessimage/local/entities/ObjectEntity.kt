package com.mygdx.guessimage.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

class ObjectData(val id: Long, val x0: Float, val y0: Float, val width: Float, val height: Float)

@Entity(
    tableName = "objects"
)
class ObjectEntity : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "o_id")
    var id = 0L

    @ColumnInfo(name = "o_p_id")
    var puzzleId = 0L

    @ColumnInfo(name = "o_name")
    var name: String? = null

    @ColumnInfo(name = "o_x")
    var x0 = 0f

    @ColumnInfo(name = "o_y")
    var y0 = 0f

    @ColumnInfo(name = "o_w")
    var width = 0f

    @ColumnInfo(name = "o_h")
    var height = 0f

    @Ignore
    var isGuessed = false

    fun setFrom(data: ObjectData): Boolean {
        if (id == data.id) {
            x0 = data.x0
            y0 = data.y0
            width = data.width
            height = data.height
            return true
        }
        return false
    }
}