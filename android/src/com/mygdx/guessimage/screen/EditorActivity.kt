package com.mygdx.guessimage.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance
import splitties.views.dsl.core.*
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.onClick

class EditorActivity : BaseActivity() {

    private val db by instance<Database>()

    private val dataSource = dataSourceOf("")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(horizontalLayout {
            lParams(matchParent, matchParent)
            addView(imageView {
                lParams(0, matchParent, weight = 3f)
                background = Color.BLUE
            })
            button {
                onClick {
                    val requestCode = if (which == 0) REQUEST_CAMERA else REQUEST_GALLERY
                    startActivityForResult(Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_GET_CONTENT
                        type = "image/*"
                    }, "Выберите приложение"), requestCode)
                }
            }
            addView(recyclerView {
                lParams(0, matchParent, weight = 1f)
                setHasFixedSize(true)
                setup {
                    withLayoutManager(LinearLayoutManager(context))
                    withDataSource(dataSource)
                    withItem<String, DummyHolder>(R.layout.item_new) {
                        onBind(::DummyHolder) { _, _ ->
                        }
                        onClick { index ->
                        }
                    }
                    withItem<PuzzleEntity, PuzzleViewHolder>(R.layout.item_puzzle) {
                        onBind(::PuzzleViewHolder) { index, item ->
                            pictogram.load(item.path)
                            count.text = "10"
                        }
                        onClick { index ->
                        }
                    }
                }
            })
        })
    }

    override fun onStart() {
        super.onStart()
        job.cancelChildren()
        launch {
            val items = withContext(Dispatchers.IO) {
                db.puzzleDao().getAll()
            }
            dataSource.apply {
                clear()
                add("")
                addAll(items)
                invalidateAll()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA ->
                    onPhotoPath(PathCompat.getPhotoFile(applicationContext)?.path)
                REQUEST_GALLERY ->
                    presenter.getGalleryPath(applicationContext, data?.data ?: return)
            }
        }
    }
}