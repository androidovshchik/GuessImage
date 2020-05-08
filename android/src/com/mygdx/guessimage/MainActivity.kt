package com.mygdx.guessimage

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleCount
import kotlinx.android.synthetic.main.item_puzzle.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.recyclerview.recyclerView

class PuzzleViewHolder(itemView: View) : ViewHolder(itemView) {
    val pictogram: ImageView = itemView.iv_pictogram
    val count: TextView = itemView.tv_count
}

class MainActivity : BaseActivity() {

    private val db by instance<Database>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataSource = dataSourceOf("")
        setContentView(frameLayout {
            recyclerView {
                setup {
                    withLayoutManager(
                        GridLayoutManager(
                            context,
                            resources.getInteger(R.integer.columns)
                        )
                    )
                    withDataSource(dataSource)
                    withItem<String, ViewHolder>(R.layout.item_new) {
                        onClick { index ->
                        }
                    }
                    withItem<PuzzleCount, PuzzleViewHolder>(R.layout.item_puzzle) {
                        onBind(::PuzzleViewHolder) { index, item ->

                        }
                        onClick { index ->
                        }
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        job.cancelChildren()
        launch {
            withContext(Dispatchers.IO) {
                db.puzzleDao().getAll()
            }
        }
    }
}
