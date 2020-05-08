package com.mygdx.guessimage.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.mygdx.guessimage.local.entities.ObjectEntity

@Dao
abstract class ObjectDao {

    @Query(
        """
        SELECT * FROM objects
        ORDER BY o_id ASC
    """
    )
    abstract fun getAll(): List<ObjectEntity>
}