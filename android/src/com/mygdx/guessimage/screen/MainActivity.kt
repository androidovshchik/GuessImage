package com.mygdx.guessimage.screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import splitties.dimensions.dip
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(frameLayout {
            lParams(matchParent, matchParent)
            addView(recyclerView {
                lParams(matchParent, matchParent)
                setHasFixedSize(true)
                val columns = resources.getInteger(R.integer.columns)
                addItemDecoration(PuzzleDecoration(applicationContext, columns))
                setup {
                    withLayoutManager(GridLayoutManager(context, columns))
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

class PuzzleDecoration(context: Context, private val columns: Int) : RecyclerView.ItemDecoration() {

    private val minSpace = context.dip(3)

    private val maxSpace = minSpace * 2

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        outRect.apply {
            top = if (position / columns == 0) maxSpace else minSpace
            left = if (position % columns == 0) maxSpace else minSpace
            right = if ((position + 1) % columns == 0) maxSpace else minSpace
            bottom = if (position / columns == state.itemCount / columns) maxSpace else minSpace
        }
    }
}
