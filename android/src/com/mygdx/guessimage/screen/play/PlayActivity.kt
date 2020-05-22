package com.mygdx.guessimage.screen.play

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidXFragmentApplication
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.UI
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.kodein.di.generic.instance

class PlayActivity : BaseActivity(), AndroidXFragmentApplication.Callbacks {

    private val db by instance<Database>()

    private lateinit var playModel: PlayModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playModel = ViewModelProvider(this).get(PlayModel::class.java).also {
            it.puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
        }
        val idPlay = View.generateViewId()
        val idList = View.generateViewId()
        setContentView(UI {
            linearLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idPlay
                }.lparams(0, matchParent, 3f)
                frameLayout {
                    id = idList
                }.lparams(0, matchParent, 1f)
            }
        }.view)
        val playFragment = PlayFragment.newInstance()
        val listFragment = ListFragment.newInstance()
        supportFragmentManager.transact {
            add(idPlay, playFragment, PlayFragment.TAG)
            add(idList, listFragment, ListFragment.TAG)
        }
        launch {
            val items = withContext(Dispatchers.IO) {
                db.objectDao().getAllByPuzzle(playModel.puzzle.id)
            }
            playFragment.setObjects(items)
            listFragment.setObjects(items)
        }
    }

    fun showWinAlert() {
        AlertDialog.Builder(this)
            .setTitle(R.string.win)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    override fun exit() {}
}