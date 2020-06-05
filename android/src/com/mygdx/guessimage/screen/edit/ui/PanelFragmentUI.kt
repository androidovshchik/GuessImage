package com.mygdx.guessimage.screen.edit.ui

import android.animation.LayoutTransition
import android.app.Activity
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.activityCallback
import com.mygdx.guessimage.extension.isVisible
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.edit.ObjectViewHolder
import com.mygdx.guessimage.screen.edit.PanelFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk21.listeners.textChangedListener

class PanelFragmentUI : AnkoComponent<PanelFragment> {

    override fun createView(ui: AnkoContext<PanelFragment>): View = with(ui) {
        verticalLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            layoutTransition = LayoutTransition()
            button {
                text = context.getString(R.string.btn_close)
                setOnClickListener {
                    ctx.activityCallback<Activity> {
                        finish()
                    }
                }
            }.lparams(matchParent, wrapContent)
            owner.buttonAdd = button {
                text = context.getString(R.string.btn_add)
                isEnabled = false
                setOnClickListener {
                    owner.addObject()
                }
            }.lparams(matchParent, wrapContent)
            owner.buttonReady = button {
                text = context.getString(R.string.btn_ready)
                isVisible = false
                setOnClickListener {
                    owner.saveObject()
                }
            }.lparams(matchParent, wrapContent)
            owner.editName = editText {
                inputType = InputType.TYPE_CLASS_TEXT
                gravity = Gravity.CENTER
                maxLines = 1
                isVisible = false
                textChangedListener {
                    afterTextChanged {
                        owner.lastObject?.name = it?.toString()
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
                        }
                    }
                }
            }.lparams(matchParent, 0, 1f)
            owner.buttonSave = button {
                text = context.getString(R.string.btn_save)
                isEnabled = false
                setOnClickListener {
                    owner.savePuzzle()
                }
            }.lparams(matchParent, wrapContent)
        }
    }
}