package com.mygdx.guessimage.screen.edit

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.backends.android.AndroidXFragmentApplication
import com.mygdx.guessimage.extension.transact
import com.mygdx.guessimage.local.FileManager
import com.mygdx.guessimage.local.PathCompat
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import com.mygdx.guessimage.screen.edit.ui.EditActivityUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.setContentView
import org.kodein.di.generic.instance

class EditActivity : BaseActivity(), AndroidXFragmentApplication.Callbacks {

    private val fileManager by instance<FileManager>()

    private lateinit var editModel: EditModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editModel = ViewModelProvider(this).get(EditModel::class.java).also {
            it.puzzle = intent.getSerializableExtra("puzzle") as PuzzleEntity
        }
        EditActivityUI().setContentView(this)
        supportFragmentManager.transact {
            add(EditActivityUI.idEdit, EditFragment.newInstance(), EditFragment.TAG)
            add(EditActivityUI.idPanel, PanelFragment.newInstance(), PanelFragment.TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EditFragment.REQUEST_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    val uri = data?.data ?: return
                    launch {
                        val filename = editModel.puzzle.filename
                        val copied = withContext(Dispatchers.IO) {
                            val path = PathCompat.getFilePath(applicationContext, uri)
                            fileManager.copyImage(path, filename)
                        }
                        if (copied) {
                            editModel.galleryPath.value = fileManager.getImageFile(filename).path
                        }
                    }
                }
            }
        }
    }

    override fun exit() {}
}