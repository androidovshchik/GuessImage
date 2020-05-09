package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance
import splitties.views.dsl.core.*
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.onClick

class ObjectViewHolder(itemView: View) : ViewHolder(itemView) {

}

class ObjectsFragment : BaseFragment() {

    private val db by instance<Database>()

    private val dataSource = dataSourceOf("")

    @Suppress("DEPRECATION")
    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return with(activity) {
            verticalLayout {
                addView(button {
                    lParams(matchParent, wrapContent)
                    text = getString(R.string.btn_close)
                    onClick {
                        finish()
                    }
                })
                addView(NestedScrollView(applicationContext).apply {
                    layoutParams = LinearLayout.LayoutParams(matchParent, 0, 1f)
                    addView(recyclerView {
                        layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
                        setHasFixedSize(true)
                        setup {
                            withLayoutManager(LinearLayoutManager(context))
                            withDataSource(dataSource)
                            withItem<ObjectEntity, ObjectViewHolder>(R.layout.item_object) {
                                onBind(::ObjectViewHolder) { index, item ->
                                }
                                onClick { index ->
                                }
                            }
                        }
                    })
                })
                addView(button {
                    lParams(matchParent, wrapContent)
                    text = getString(R.string.btn_save)
                    onClick {

                    }
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            val items = withContext(Dispatchers.IO) {
                db.objectDao().getAll()
            }
            dataSource.apply {
                clear()
                invalidateAll()
            }
        }
    }
}