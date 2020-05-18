package com.mygdx.guessimage.screen.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mygdx.guessimage.Mode
import com.mygdx.guessimage.local.entities.ObjectEntity
import com.mygdx.guessimage.local.entities.PuzzleEntity

class PuzzleModel : ViewModel() {

    lateinit var mode: Mode

    lateinit var puzzle: PuzzleEntity

    val currentObj = MutableLiveData<ObjectEntity>()

    val galleryUri = MutableLiveData<String>()
}