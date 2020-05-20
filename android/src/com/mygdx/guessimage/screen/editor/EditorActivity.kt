package com.mygdx.guessimage.screen.editor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.FileManager
import com.mygdx.guessimage.local.PathCompat
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.kodein.di.generic.instance

class EditorActivity : BaseActivity(), AndroidFragmentApplication.Callbacks {

    private val fileManager by instance<FileManager>()

    private lateinit var puzzleModel: PuzzleModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
        puzzleModel = ViewModelProvider(this).get(PuzzleModel::class.java).also {
            it.mode = if (puzzle.id > 0) Mode.PLAY else Mode.EDIT
            it.puzzle = puzzle
        }
        val idObject = View.generateViewId()
        val idObjects = View.generateViewId()
        setContentView(UI {
            linearLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idObject
                }.lparams(0, matchParent, 3f)
                frameLayout {
                    id = idObjects
                }.lparams(0, matchParent, 1f)
            }
        }.view)
        supportFragmentManager.transact {
            add(idObject, ObjectFragment.newInstance(), ObjectFragment.TAG)
            add(idObjects, ObjectsFragment.newInstance(), ObjectsFragment.TAG)
        }
    }

    fun showWin() {
        alert("Вы выиграли!") {
            okButton {
                finish()
            }
        }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ObjectFragment.REQUEST_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    val uri = data?.data ?: return
                    launch {
                        val filename = withContext(Dispatchers.IO) {
                            val path = PathCompat.getFilePath(applicationContext, uri)
                            fileManager.copyImage(path)
                        }
                        if (!filename.isNullOrBlank()) {
                            puzzleModel.apply {
                                puzzle.filename = filename
                                galleryPath.value = fileManager.getImageFile(filename).path
                            }
                        }
                    }
                }
            }
        }
    }

    override fun exit() {}
}