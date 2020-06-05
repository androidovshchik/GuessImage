package com.mygdx.guessimage.screen.edit.ui

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.isVisible
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.edit.ObjectViewHolder
import com.mygdx.guessimage.screen.edit.PanelFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedButton
import org.jetbrains.anko.sdk21.listeners.textChangedListener

class PanelFragmentUI : AnkoComponent<PanelFragment> {

    override fun createView(ui: AnkoContext<PanelFragment>): View = with(ui) {
        owner.addAnimation = ValueAnimator.ofInt(0, greyToPink.lastIndex).apply {
            duration = 500L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                owner.buttonAdd.backgroundTintList =
                    ColorStateList.valueOf(greyToPink[it.animatedValue as Int])
            }
        }
        verticalLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            layoutTransition = LayoutTransition()
            button {
                text = context.getString(R.string.btn_close)
                setOnClickListener {
                    owner.closePuzzle()
                }
            }.lparams(matchParent, wrapContent)
            owner.buttonAdd = tintedButton {
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

    companion object {

        // https://colordesigner.io/gradient-generator
        private val greyToPink = intArrayOf(
            Color.parseColor("#d6d8d7"),
            Color.parseColor("#ced6d0"),
            Color.parseColor("#c9d4c7"),
            Color.parseColor("#c6d1bc"),
            Color.parseColor("#c7cdb0"),
            Color.parseColor("#cbc8a4"),
            Color.parseColor("#d1c399"),
            Color.parseColor("#d9bc8f"),
            Color.parseColor("#e1b488"),
            Color.parseColor("#eaac85"),
            Color.parseColor("#f2a385"),
            Color.parseColor("#f99989"),
            Color.parseColor("#fe9091"),
            Color.parseColor("#ff879c"),
            Color.parseColor("#ff80ab")
        )
    }
}