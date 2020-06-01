package com.mygdx.guessimage.screen.play.ui

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.activityCallback
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.model.Frame
import com.mygdx.guessimage.screen.edit.ObjectViewHolder
import com.mygdx.guessimage.screen.play.ListFragment
import org.jetbrains.anko.*

class ListFragmentUI : AnkoComponent<ListFragment> {

    override fun createView(ui: AnkoContext<ListFragment>): View = with(ui) {
        verticalLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            button {
                text = context.getString(R.string.btn_close)
                setOnClickListener {
                    ctx.activityCallback<Activity> {
                        finish()
                    }
                }
            }.lparams(matchParent, wrapContent)
            recyclerView {
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                setup {
                    withLayoutManager(LinearLayoutManager(context))
                    withDataSource(owner.dataSource)
                    withItem<ObjectEntity, ObjectViewHolder>(R.layout.item_object) {
                        onBind(::ObjectViewHolder) { _, item ->
                            name.text = item.name
                            itemView.setBackgroundColor(
                                if (item.isGuessed) {
                                    Color.parseColor(Frame.GREEN)
                                } else {
                                    Color.TRANSPARENT
                                }
                            )
                        }
                    }
                }
            }.lparams(matchParent, 0, 1f)
        }
    }
}