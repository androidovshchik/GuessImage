package com.mygdx.guessimage

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import coil.Coil
import coil.ImageLoader
import com.mygdx.guessimage.local.Database
import okhttp3.OkHttpClient
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
        GdxLog.DEBUG = BuildConfig.DEBUG
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Class.forName("com.facebook.stetho.Stetho")
                .getDeclaredMethod("initializeWithDefaults", Context::class.java)
                .invoke(null, applicationContext)
        }
        Coil.setImageLoader(
            ImageLoader.Builder(applicationContext)
                .availableMemoryPercentage(0.5)
                .bitmapPoolPercentage(0.5)
                .okHttpClient {
                    OkHttpClient.Builder()
                        .cache(null)
                        .build()
                }
                .build()
        )
    }
}