package com.mygdx.guessimage.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mygdx.guessimage.local.entities.PuzzleCount
import com.mygdx.guessimage.local.entities.PuzzleEntity

@Dao
abstract class PuzzleDao {

    @Query(
        """
        SELECT puzzles.*, count() AS count FROM puzzles
        LEFT JOIN objects ON p_id = o_p_id
        GROUP BY o_p_id
        ORDER BY p_id ASC
    """
    )
    abstract fun getAllCounted(): List<PuzzleCount>

    @Insert
    abstract fun insert(item: PuzzleEntity)
}