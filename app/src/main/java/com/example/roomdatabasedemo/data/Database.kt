package com.example.roomdatabasedemo.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.roomdatabasedemo.model.Repository


@androidx.room.Database(entities = [Repository::class],version = 1,exportSchema = false)
abstract class Database() : RoomDatabase(){

abstract  fun userDao():DatabaseDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}