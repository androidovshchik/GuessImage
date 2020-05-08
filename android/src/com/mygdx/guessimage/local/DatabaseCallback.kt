package com.mygdx.guessimage.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class DatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(sqliteDatabase: SupportSQLiteDatabase) = sqliteDatabase.run {
        beginTransaction()
        try {
            (1..30).forEach {
                execSQL("INSERT INTO puzzles(p_id, p_path) VALUES($it, 'file:///android_asset/pictogram.png')")
            }
            setTransactionSuccessful()
        } finally {
            endTransaction()
        }
    }
}