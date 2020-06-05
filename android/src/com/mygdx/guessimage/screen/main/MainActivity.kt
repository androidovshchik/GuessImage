package com.mygdx.guessimage.screen.main

import android.os.Bundle
import com.afollestad.recyclical.datasource.dataSourceOf
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import com.mygdx.guessimage.screen.edit.EditActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.kodein.di.generic.instance
import java.util.*

class MainActivity : BaseActivity() {

    private val db by instance<Database>()

    val dataSource = dataSourceOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI(applicationContext).setContentView(this)
    }

    fun createPuzzle() {
        isTouchable = false
        launch {
            val puzzle = PuzzleEntity()
            puzzle.filename = UUID.randomUUID().toString()
            withContext(Dispatchers.IO) {
                puzzle.id = db.puzzleDao().insert(puzzle)
            }
            startPuzzle(puzzle)
            isTouchable = true
        }
    }

    fun startPuzzle(puzzle: PuzzleEntity) {
        startActivity<EditActivity>("puzzle" to puzzle)
    }

    override fun onStart() {
        super.onStart()
        job.cancelChildren()
        launch {
            val items = withContext(Dispatchers.IO) {
                db.puzzleDao().getReadyCounted()
            }
            dataSource.apply {
                clear()
                add("")
                addAll(items)
                invalidateAll()
            }
        }
    }
}