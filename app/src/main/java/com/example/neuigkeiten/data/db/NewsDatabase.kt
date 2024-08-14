package com.example.neuigkeiten.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsArticle::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}