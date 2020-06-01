package com.mygdx.guessimage.screen.main

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.entities.PuzzleCount
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import kotlinx.android.synthetic.main.item_puzzle.view.*
import org.jetbrains.anko.*

class DummyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class PuzzleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.iv_icon
    val count: TextView = itemView.tv_count
}

class MainActivityUI : AnkoComponent<MainActivity> {

    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        frameLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            recyclerView {
                val columns = resources.getInteger(R.integer.columns)
                addItemDecoration(LayoutMarginDecoration(columns, dip(6)).also {
                    it.setPadding(this, dip(6))
                })
                setup {
                    withLayoutManager(GridLayoutManager(context, columns))
                    withDataSource(owner.dataSource)
                    withItem<String, DummyHolder>(R.layout.item_guess) {
                        onBind(::DummyHolder) { _, _ ->
                        }
                        onClick {
                            owner.createGame()
                        }
                    }
                    withItem<PuzzleCount, PuzzleViewHolder>(R.layout.item_puzzle) {
                        onBind(::PuzzleViewHolder) { _, item ->
                            icon.load(owner.fileManager.getIconFile(item.puzzle.filename))
                            count.text = item.count.toString()
                        }
                        onClick {
                            owner.startGame(item.puzzle)
                        }
                    }
                }
            }.lparams(matchParent, matchParent)
        }
    }
}