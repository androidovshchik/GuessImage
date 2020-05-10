package com.mygdx.guessimage.screen.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.mygdx.guessimage.local.entities.PuzzleEntity

class PuzzleModel(private val puzzle: PuzzleEntity) : ViewModel() {

    val articleList: LiveData<List<Article>>? = null

    var selectedArticle: MutableLiveData<Article> = MutableLiveData<Article>()
        set(article) {
            selectedArticle.setValue(article)
        }

    fun loadArticles() {
        // fetch articles here asynchronously
    }

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(private val puzzle: PuzzleEntity) : NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PuzzleModel(puzzle) as T
        }
    }
}