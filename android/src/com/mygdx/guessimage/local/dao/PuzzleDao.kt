package com.mygdx.guessimage.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.mygdx.guessimage.local.entities.PuzzleEntity

@Dao
abstract class PuzzleDao {

    @Query(
        """
        SELECT * FROM puzzles
        ORDER BY p_id ASC
    """
    )
    abstract fun getAll(): List<PuzzleEntity>
}