package com.mygdx.guessimage

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mygdx.guessimage.local.Database
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

@Suppress("unused")
class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<Context>() with provider {
            applicationContext
        }

        bind<Database>() with singleton {
            Room.databaseBuilder(instance(), Database::class.java, "app.db")
                .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}