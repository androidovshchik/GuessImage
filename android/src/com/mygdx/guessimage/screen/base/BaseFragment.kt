@file:Suppress("DEPRECATION")

package com.mygdx.guessimage.screen.base

import android.app.Fragment
import android.content.Context
import com.mygdx.guessimage.extension.isMarshmallowPlus
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import timber.log.Timber

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment : Fragment(), KodeinAware, CoroutineScope {

    override val kodein by closestKodein()

    protected val job = SupervisorJob()

    protected val appContext: Context?
        get() = if (isMarshmallowPlus()) context else activity?.applicationContext

    override fun onDestroyView() {
        job.cancelChildren()
        super.onDestroyView()
    }

    override val coroutineContext = Dispatchers.Main + job + CoroutineExceptionHandler { _, e ->
        Timber.e(e)
    }
}