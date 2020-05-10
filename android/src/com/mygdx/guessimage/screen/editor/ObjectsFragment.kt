package com.mygdx.guessimage.screen.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.nestedScrollView
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.android.synthetic.main.item_object.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.button
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent
import org.kodein.di.generic.instance

class ObjectViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.tv_name
}

class ObjectsFragment : BaseFragment() {

    private lateinit var puzzleModel: PuzzleModel

    private val db by instance<Database>()

    private val dataSource = dataSourceOf()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        puzzleModel = ViewModelProvider(this).get(PuzzleModel::class.java)
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
                nestedScrollView {
                    verticalLayout {
                        layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
                        recyclerView {
                            isNestedScrollingEnabled = false
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
                        }.lparams(matchParent, wrapContent)
                        button {
                            text = getString(R.string.btn_add)
                            setOnClickListener {
                                appContext?.activityCallback<EditorActivity> {
                                    editObject(ObjectEntity())
                                }
                            }
                        }.lparams(matchParent, wrapContent)
                    }
                }.lparams(matchParent, 0, 1f)
                button {
                    text = getString(R.string.btn_save)
                    setOnClickListener {

                    }
                }.lparams(matchParent, wrapContent)
            }
        }.view
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

    companion object {

        val TAG = ObjectsFragment::class.java.simpleName

        fun newInstance(): ObjectsFragment {
            return ObjectsFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}