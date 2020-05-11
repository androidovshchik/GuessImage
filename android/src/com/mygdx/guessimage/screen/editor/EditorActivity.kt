package com.mygdx.guessimage.screen.editor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.FileManager
import com.mygdx.guessimage.local.PathCompat
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
import java.io.File

class EditorActivity : BaseActivity(), AndroidFragmentApplication.Callbacks {

    private val fileManager by instance<FileManager>()

    private lateinit var puzzleModel: PuzzleModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        puzzleModel = ViewModelProvider(this).get(PuzzleModel::class.java)
        puzzleModel.puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ObjectFragment.REQUEST_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    val uri = data?.data ?: return
                    launch {
                        val (path, filename) = withContext(Dispatchers.IO) {
                            val path = PathCompat.getFilePath(applicationContext, uri)
                            path to fileManager.copyImage(File(path.orEmpty()))
                        }
                        puzzleModel.apply {
                            puzzle.filename = filename
                            galleryUri.value = "file://$path"
                        }
                    }
                }
            }
        }
    }

    override fun exit() {}
}