package com.mygdx.guessimage.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.FileManager
import com.mygdx.guessimage.local.entities.PuzzleCount
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.base.BaseActivity
import com.mygdx.guessimage.screen.editor.EditorActivity
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import kotlinx.android.synthetic.main.item_puzzle.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.kodein.di.generic.instance
import java.io.File

class DummyHolder(itemView: View) : ViewHolder(itemView)

class PuzzleViewHolder(itemView: View) : ViewHolder(itemView) {
    val icon: ImageView = itemView.iv_icon
    val count: TextView = itemView.tv_count
}

class MainActivity : BaseActivity() {

    private val db by instance<Database>()

    private val fileManager by instance<FileManager>()

    private val dataSource = dataSourceOf("")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(UI {
            frameLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                recyclerView {
                    val columns = resources.getInteger(R.integer.columns)
                    addItemDecoration(LayoutMarginDecoration(columns, dip(6)).also {
                        it.setPadding(this, dip(6))
                    })
                    setup {
                        withLayoutManager(GridLayoutManager(context, columns))
                        withDataSource(dataSource)
                        withItem<String, DummyHolder>(R.layout.item_guess) {
                            onBind(::DummyHolder) { _, _ ->
                            }
                            onClick {
                                startActivity<EditorActivity>(
                                    "puzzle" to PuzzleEntity()
                                )
                            }
                        }
                        withItem<PuzzleCount, PuzzleViewHolder>(R.layout.item_puzzle) {
                            onBind(::PuzzleViewHolder) { _, item ->
                                val filename = item.puzzle.filename.orEmpty()
                                icon.load(File(fileManager.iconsDir, filename))
                                count.text = item.count.toString()
                            }
                            onClick { index ->
                                startActivity<EditorActivity>(
                                    "puzzle" to dataSource[index]
                                )
                            }
                        }
                    }
                }.lparams(matchParent, matchParent)
            }
        }.view)
    }

    override fun onStart() {
        super.onStart()
        job.cancelChildren()
        launch {
            val items = withContext(Dispatchers.IO) {
                db.puzzleDao().getAllCounted()
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