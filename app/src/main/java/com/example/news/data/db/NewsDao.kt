package com.example.news.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newsArticle: NewsArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticle>)

    @Query("SELECT * FROM news_articles WHERE url = :url LIMIT 1")
    fun getArticleByUrl(url: String): NewsArticle?

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC LIMIT 5")
    fun getSlideshowItems(): LiveData<List<NewsArticle>>


    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getNewsArticles(): PagingSource<Int, NewsArticle>

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getAllArticles(): List<NewsArticle>

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getNewsPagingSource(): PagingSource<Int, NewsArticle>

    @Query("DELETE FROM news_articles")
    suspend fun clearAllNews()

    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getAllNewsArticles(): PagingSource<Int, NewsArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticleToFavorites(article: NewsArticle)

    @Query("SELECT * FROM news_articles WHERE isFavorite = 1 ORDER BY publishedAt DESC")
    fun getFavoriteArticles(): PagingSource<Int, NewsArticle>

    @Query("SELECT * FROM news_articles WHERE title LIKE :query ORDER BY publishedAt DESC")
    fun searchNewsArticles(query: String): PagingSource<Int, NewsArticle>

    @Update
    suspend fun updateNewsArticle(article: NewsArticle)

    @Query("DELETE FROM news_articles WHERE url = :url")
    suspend fun deleteByUrl(url: String)
}