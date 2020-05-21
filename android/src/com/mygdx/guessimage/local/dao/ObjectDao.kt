package com.mygdx.guessimage.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mygdx.guessimage.local.entities.ObjectEntity

@Dao
abstract class ObjectDao {

    @Query(
        """
        SELECT * FROM objects
        WHERE o_p_id = :id
        ORDER BY o_id ASC
    """
    )
    abstract fun getAllByPuzzle(id: Long): List<ObjectEntity>

    @Insert
    abstract fun insert(item: ObjectEntity): Long

    @Update
    abstract fun update(item: ObjectEntity)
}