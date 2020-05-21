package com.mygdx.guessimage.screen.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.local.entities.PuzzleEntity
import com.mygdx.guessimage.screen.ObjectData

class EditModel : ViewModel() {

    lateinit var puzzle: PuzzleEntity

    val currentObject = MutableLiveData<ObjectEntity>()

    val galleryPath = MutableLiveData<String>()

    val frameChanged = MutableLiveData<ObjectData>()
}