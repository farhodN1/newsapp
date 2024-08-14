package com.example.neuigkeiten.data.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticle>)

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC LIMIT :limit")
    fun getTopNewsArticles(limit: Int): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getNewsArticles(): PagingSource<Int, NewsArticle>

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getNewsPagingSource(): PagingSource<Int, NewsArticle>

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getAllNewsArticles(): PagingSource<Int, NewsArticle>

    @Query("SELECT * FROM news_articles WHERE isFavorite = 1 ORDER BY publishedAt DESC")
    fun getFavoriteArticles(): PagingSource<Int, NewsArticle>

    @Query("SELECT * FROM news_articles WHERE title LIKE :query ORDER BY publishedAt DESC")
    fun searchNewsArticles(query: String): PagingSource<Int, NewsArticle>

    @Update
    suspend fun updateNewsArticle(article: NewsArticle)
}