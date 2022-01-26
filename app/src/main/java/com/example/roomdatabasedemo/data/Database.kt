package com.example.roomdatabasedemo.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.roomdatabasedemo.model.Repository
import com.example.roomdatabasedemo.model.Repository1


@androidx.room.Database(entities = [Repository::class,Repository1::class],version = 2,exportSchema = false)
abstract class Database() : RoomDatabase(){

abstract  fun userDao():DatabaseDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: Database? = null

        private val migration:Migration=object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'user' ADD COLUMN 'number' TEXT")
            }

        }

        fun getDatabase(context: Context): Database {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "user_database"
                )
                //this method used clean database. data all remove and new version database apply
                    //.fallbackToDestructiveMigration()
                    .addMigrations(migration)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}