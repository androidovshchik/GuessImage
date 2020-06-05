package com.mygdx.guessimage.screen.edit

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.emptyDataSourceTyped
import com.mygdx.guessimage.R
import com.mygdx.guessimage.extension.isVisible
import com.mygdx.guessimage.local.Database
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.screen.base.BaseFragment
import com.mygdx.guessimage.screen.edit.ui.PanelFragmentUI
import kotlinx.android.synthetic.main.item_object.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoContext
import org.kodein.di.generic.instance

class ObjectViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.tv_name
}

class PanelFragment : BaseFragment() {

    private val db by instance<Database>()

    private lateinit var editModel: EditModel

    lateinit var buttonAdd: Button
    lateinit var buttonReady: Button
    lateinit var editName: EditText
    lateinit var buttonSave: Button

    val dataSource = emptyDataSourceTyped<ObjectEntity>()

    lateinit var colorAnimation: ValueAnimator

    var lastObject: ObjectEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editModel = ViewModelProvider(requireActivity()).get(EditModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View {
        return PanelFragmentUI().createView(AnkoContext.create(requireActivity(), this))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editModel.galleryPath.observe(viewLifecycleOwner, Observer {
            buttonAdd.isEnabled = true
            buttonSave.isEnabled = true
            colorAnimation.start()
        })
        editModel.frameChanged.observe(viewLifecycleOwner, Observer {
            lastObject?.setFrom(it)
            editName.requestFocus()
        })
    }

    fun addObject() {
        isTouchable = false
        colorAnimation.cancel()
        launch {
            val objectEntity = ObjectEntity()
            objectEntity.puzzleId = editModel.puzzle.id
            withContext(Dispatchers.IO) {
                objectEntity.id = db.objectDao().insert(objectEntity)
            }
            lastObject = objectEntity
            editModel.newObject.value = objectEntity
            buttonAdd.isVisible = false
            editName.apply {
                text = null
                isVisible = true
                requestFocus()
            }
            buttonReady.isVisible = true
            isTouchable = true
        }
    }

    fun saveObject() {
        val objectEntity = lastObject ?: return
        isTouchable = false
        launch {
            withContext(Dispatchers.IO) {
                db.objectDao().update(objectEntity)
            }
            editModel.readyObject.value = objectEntity
            dataSource.apply {
                add(objectEntity)
                invalidateAt(size() - 1)
            }
            lastObject = null
            editName.isVisible = false
            buttonReady.isVisible = false
            buttonAdd.isVisible = true
            isTouchable = true
        }
    }

    fun savePuzzle() {
        isTouchable = false
        val puzzle = editModel.puzzle
        puzzle.ready = true
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                db.puzzleDao().update(puzzle)
            }
            activity?.finish()
        }
    }

    private fun showPromptAlert() {
        val activity = activity ?: return
        AlertDialog.Builder(activity)
            .setTitle(R.string.prompt)
            .setPositiveButton(android.R.string.ok, null)
            .show()
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