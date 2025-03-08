package com.lloydsbyte.redditassesment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lloydsbyte.redditassesment.network.CharacterResponseModel

@Database(entities = [CharacterResponseModel.CharacterModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao


    companion object {
        private var instance: AppDatabase? = null
        private val DB_NAME = "com.lloydsbyte.redditassesment.db"

        fun getDatabase(applicationContext: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}