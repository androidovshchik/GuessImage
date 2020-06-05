package com.mygdx.guessimage.screen.play

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidXFragmentApplication
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import com.mygdx.guessimage.screen.play.ui.PlayActivityUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.setContentView
import org.kodein.di.generic.instance

class PlayActivity : BaseActivity(), AndroidXFragmentApplication.Callbacks {

    private val db by instance<Database>()

    private lateinit var playModel: PlayModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playModel = ViewModelProvider(this).get(PlayModel::class.java).also {
            it.puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
        }
        PlayActivityUI().setContentView(this)
        val playFragment = PlayFragment.newInstance()
        val listFragment = ListFragment.newInstance()
        supportFragmentManager.transact {
            add(PlayActivityUI.idPlay, playFragment, PlayFragment.TAG)
            add(PlayActivityUI.idList, listFragment, ListFragment.TAG)
        }
        launch {
            val items = withContext(Dispatchers.IO) {
                db.objectDao().getAllByPuzzle(playModel.puzzle.id)
            }
            playFragment.setObjects(items)
            listFragment.setObjects(items)
        }
    }

    override fun exit() {}
}