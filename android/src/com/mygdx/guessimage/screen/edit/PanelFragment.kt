package com.mygdx.guessimage.screen.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.FileManager
import com.mygdx.guessimage.local.deleteFile
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.android.synthetic.main.item_object.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

class PanelFragment : BaseFragment() {

    private val db by instance<Database>()

    private val fileManager by instance<FileManager>()

    private lateinit var editModel: EditModel

    private var buttonAdd: Button? = null

    private var buttonSave: Button? = null

    private val dataSource = emptyDataSourceTyped<ObjectEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editModel = ViewModelProvider(requireActivity()).get(EditModel::class.java)
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
                buttonAdd = button {
                    text = getString(R.string.btn_add)
                    isEnabled = !editModel.puzzle.filename.isNullOrBlank()
                    setOnClickListener {
                        val obj = ObjectEntity()
                        dataSource.apply {
                            add(obj)
                            invalidateAt(size() - 1)
                        }
                        editModel.currentObject.value = obj
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
                            }
                            onClick { index ->
                                editModel.currentObject.value = dataSource[index]
                            }
                        }
                    }
                }.lparams(matchParent, 0, 1f)
                buttonSave = button {
                    text = getString(R.string.btn_save)
                    isEnabled = !editModel.puzzle.filename.isNullOrBlank()
                    setOnClickListener {
                        isTouchable = false
                        val puzzle = editModel.puzzle
                        val objects = dataSource.toList()
                        GlobalScope.launch(Dispatchers.Main) {
                            if (puzzle.id == 0L) {
                                withContext(Dispatchers.IO) {
                                    puzzle.id = db.puzzleDao().insert(puzzle)
                                    objects.forEach {
                                        it.puzzleId = puzzle.id
                                    }
                                    db.objectDao().insert(objects)
                                }
                            }
                            activity?.finish()
                        }
                    }
                }.lparams(matchParent, wrapContent)
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editModel.galleryPath.observe(viewLifecycleOwner, Observer {
            buttonAdd?.isEnabled = true
            buttonSave?.isEnabled = true
        })
        launch {
            val items = withContext(Dispatchers.IO) {
                db.objectDao().getAllByPuzzle(editModel.puzzle.id)
            }
            dataSource.apply {
                clear()
                addAll(items)
                invalidateAll()
            }
        }
    }

    override fun onDestroy() {
        val puzzle = editModel.puzzle
        if (puzzle.id == 0L && puzzle.filename != null) {
            val filename = puzzle.filename
            val imageFile = fileManager.getImageFile(filename)
            val iconFile = fileManager.getIconFile(filename)
            GlobalScope.launch(Dispatchers.IO) {
                deleteFile(imageFile)
                deleteFile(iconFile)
            }
        }
        super.onDestroy()
    }

    companion object {

        val TAG = PanelFragment::class.java.simpleName

        fun newInstance(): PanelFragment {
            return PanelFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}