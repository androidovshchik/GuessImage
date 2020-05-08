package com.mygdx.guessimage

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.PuzzleEntity
import kotlinx.android.synthetic.main.item_puzzle.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.recyclerview.recyclerView

class DummyHolder(itemView: View) : ViewHolder(itemView)

class PuzzleViewHolder(itemView: View) : ViewHolder(itemView) {
    val pictogram: ImageView = itemView.iv_pictogram
    val count: TextView = itemView.tv_count
}

class MainActivity : BaseActivity() {

    private val db by instance<Database>()

    private val dataSource = dataSourceOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(frameLayout {
            lParams(matchParent, matchParent)
            addView(recyclerView {
                lParams(matchParent, matchParent)
                setup {
                    withLayoutManager(
                        GridLayoutManager(
                            context,
                            resources.getInteger(R.integer.columns)
                        )
                    )
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
}
