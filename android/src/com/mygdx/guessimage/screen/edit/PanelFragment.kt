package com.mygdx.guessimage.screen.edit

import android.animation.LayoutTransition
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import com.mygdx.guessimage.extension.isVisible
import com.mygdx.guessimage.extension.recyclerView
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import kotlinx.android.synthetic.main.item_object.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk21.listeners.onFocusChange
import org.jetbrains.anko.sdk21.listeners.textChangedListener
import org.jetbrains.anko.support.v4.UI
import org.kodein.di.generic.instance

class ObjectViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.tv_name
}

class PanelFragment : BaseFragment() {

    private val db by instance<Database>()

    private lateinit var editModel: EditModel

    private lateinit var buttonAdd: Button
    private lateinit var buttonReady: Button
    private lateinit var editName: EditText
    private lateinit var buttonSave: Button

    private val dataSource = emptyDataSourceTyped<ObjectEntity>()

    private var lastObject: ObjectEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editModel = ViewModelProvider(requireActivity()).get(EditModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return UI {
            verticalLayout {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                layoutTransition = LayoutTransition()
                button {
                    text = getString(R.string.btn_close)
                    setOnClickListener {
                        activity?.finish()
                    }
                }.lparams(matchParent, wrapContent)
                buttonAdd = button {
                    text = getString(R.string.btn_add)
                    isEnabled = false
                    setOnClickListener {
                        isTouchable = false
                        launch {
                            val objectEntity = ObjectEntity()
                            objectEntity.puzzleId = editModel.puzzle.id
                            withContext(Dispatchers.IO) {
                                objectEntity.id = db.objectDao().insert(objectEntity)
                            }
                            lastObject = objectEntity
                            editModel.newObject.value = objectEntity
                            it.isVisible = false
                            editName.apply {
                                text = null
                                isVisible = true
                                requestFocus()
                            }
                            buttonReady.isVisible = true
                            isTouchable = true
                        }
                    }
                }.lparams(matchParent, wrapContent)
                buttonReady = button {
                    text = getString(R.string.btn_ready)
                    isVisible = false
                    setOnClickListener {
                        val objectEntity = lastObject ?: return@setOnClickListener
                        isTouchable = false
                        launch {
                            withContext(Dispatchers.IO) {
                                db.objectDao().update(objectEntity)
                            }
                            lastObject = null
                            editModel.readyObject.value = objectEntity
                            dataSource.apply {
                                add(objectEntity)
                                invalidateAt(size() - 1)
                            }
                            editName.isVisible = false
                            it.isVisible = false
                            buttonAdd.isVisible = true
                            isTouchable = true
                        }
                    }
                }.lparams(matchParent, wrapContent)
                editName = editText {
                    inputType = InputType.TYPE_CLASS_TEXT
                    gravity = Gravity.CENTER
                    maxLines = 1
                    isVisible = false
                    textChangedListener {
                        afterTextChanged {
                            lastObject?.name = it?.toString()
                        }
                    }
                    onFocusChange { v, hasFocus ->
                        if (!hasFocus) {
                            v.requestFocus()
                        }
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
                        }
                    }
                }.lparams(matchParent, 0, 1f)
                buttonSave = button {
                    text = getString(R.string.btn_save)
                    isEnabled = false
                    setOnClickListener {
                        isTouchable = false
                        GlobalScope.launch(Dispatchers.Main) {
                            val puzzle = editModel.puzzle
                            puzzle.ready = true
                            withContext(Dispatchers.IO) {
                                db.puzzleDao().update(puzzle)
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
            buttonAdd.isEnabled = true
            buttonSave.isEnabled = true
        })
        editModel.frameChanged.observe(viewLifecycleOwner, Observer {
            lastObject?.setFrom(it)
        })
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