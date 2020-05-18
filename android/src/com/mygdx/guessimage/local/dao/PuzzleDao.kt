package com.mygdx.guessimage.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.mygdx.guessimage.local.entities.PuzzleEntity

@Dao
abstract class PuzzleDao {

    @Query(
        """
        SELECT puzzles.*, count(o_id) AS count FROM puzzles
        LEFT JOIN objects ON p_id = o_p_id
        GROUP BY o_p_id
        ORDER BY p_id ASC
    """
    )
    abstract fun getAll(): List<PuzzleEntity>
}