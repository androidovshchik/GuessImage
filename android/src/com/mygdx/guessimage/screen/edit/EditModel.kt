package com.mygdx.guessimage.screen.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mygdx.guessimage.local.entities.ObjectData
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.local.entities.PuzzleEntity

class EditModel : ViewModel() {

    lateinit var puzzle: PuzzleEntity

    val currentObject = MutableLiveData<ObjectEntity>()

    val galleryPath = MutableLiveData<String>()

    val frameChanged = MutableLiveData<ObjectData>()
}