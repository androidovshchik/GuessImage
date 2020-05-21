package com.mygdx.guessimage.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mygdx.guessimage.local.entities.PuzzleCount
import com.mygdx.guessimage.local.entities.PuzzleEntity

@Dao
abstract class PuzzleDao {

    @Query(
        """
        SELECT puzzles.*, count(o_p_id) AS count FROM puzzles
        LEFT JOIN objects ON p_id = o_p_id
        WHERE p_ready = 1
        GROUP BY p_id
        ORDER BY p_id ASC
    """
    )
    abstract fun getReadyCounted(): List<PuzzleCount>

    @Insert
    abstract fun insert(item: PuzzleEntity): Long

    @Update
    abstract fun update(item: PuzzleEntity)
}