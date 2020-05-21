package com.mygdx.guessimage.screen.play

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import com.mygdx.guessimage.screen.edit.ObjectViewHolder
import org.jetbrains.anko.button
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

class ListFragment : BaseFragment() {

    private lateinit var playModel: PlayModel

    private val dataSource = emptyDataSourceTyped<ObjectEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playModel = ViewModelProvider(requireActivity()).get(PlayModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            verticalLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                button {
                    text = getString(R.string.btn_close)
                    setOnClickListener {
                        activity?.finish()
                    }
                }.lparams(matchParent, wrapContent)
                recyclerView {
                    addItemDecoration(DividerItemDecoration(context, VERTICAL))
                    setup {
                        withLayoutManager(LinearLayoutManager(context))
                        withDataSource(dataSource)
                        withItem<ObjectEntity, ObjectViewHolder>(R.layout.item_object) {
                            onBind(::ObjectViewHolder) { _, item ->
                                name.text = item.name
                                itemView.setBackgroundColor(
                                    if (item.isGuessed) {
                                        Color.parseColor("#CDDC39")
                                    } else {
                                        Color.TRANSPARENT
                                    }
                                )
                            }
                        }
                    }
                }.lparams(matchParent, 0, 1f)
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playModel.framesGuessed.observe(viewLifecycleOwner, Observer {
            dataSource.forEach { item ->
                if (item.id in it) {
                    item.isGuessed = true
                }
            }
        })
    }

    fun setObjects(items: List<ObjectEntity>) {
        dataSource.apply {
            clear()
            addAll(items)
            invalidateAll()
        }
    }

    companion object {

        val TAG = ListFragment::class.java.simpleName

        fun newInstance(): ListFragment {
            return ListFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}