package com.mygdx.guessimage.screen.edit

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

class EditActivity : BaseActivity(), AndroidFragmentApplication.Callbacks {

    private val fileManager by instance<FileManager>()

    private lateinit var editModel: EditModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editModel = ViewModelProvider(this).get(EditModel::class.java).also {
            it.puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
        }
        val idEdit = View.generateViewId()
        val idPanel = View.generateViewId()
        setContentView(UI {
            linearLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                frameLayout {
                    id = idEdit
                }.lparams(0, matchParent, 3f)
                frameLayout {
                    id = idPanel
                }.lparams(0, matchParent, 1f)
            }
        }.view)
        supportFragmentManager.transact {
            add(idEdit, EditFragment.newInstance(), EditFragment.TAG)
            add(idPanel, PanelFragment.newInstance(), PanelFragment.TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EditFragment.REQUEST_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    val uri = data?.data ?: return
                    launch {
                        val filename = withContext(Dispatchers.IO) {
                            val path = PathCompat.getFilePath(applicationContext, uri)
                            fileManager.copyImage(path)
                        }
                        if (!filename.isNullOrBlank()) {
                            editModel.apply {
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