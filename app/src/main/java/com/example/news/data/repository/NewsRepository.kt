package com.example.news.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.news.BuildConfig
import com.example.news.data.api.NewsApi
import com.example.news.data.api.RetrofitInstance
import com.example.news.data.db.NewsDao
import com.example.news.data.db.NewsArticle
import com.example.news.data.db.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NewsRepository {

    private val api: NewsApi by lazy { RetrofitInstance.api }
    private val newsDao: NewsDao by lazy {
        NewsDatabase.getDatabase().getNewsDao()
    }

    // Network
    suspend fun fetchAndSaveNews(category: String) {
        Log.i("category", category)
        val response = api.searchNews(BuildConfig.API_KEY, category)
        if (response.isSuccessful) {
            response.body()?.articles?.let { articles ->
                val newsArticles = articles.map { article ->
                    NewsArticle(
                        title = article.title,
                        description = article.description,
                        url = article.url,
                        urlToImage = article.urlToImage,
                        publishedAt = article.publishedAt
                    )
                }
                // Insert only if the article is not already in the database
                withContext(Dispatchers.IO) {
                    newsArticles.forEach { newsArticle ->
                        if (newsDao.getArticleByUrl(newsArticle.url) == null) {
                            newsDao.insertAll(listOf(newsArticle))
                        }
                    }
                }
            }
        }
    }

    // Database
    fun getSlideshowItems(): LiveData<List<NewsArticle>> {
        return newsDao.getSlideshowItems()
    }

    fun getArticleByUrl(url: String): NewsArticle? {
        return newsDao.getArticleByUrl(url)
    }

    fun getNewsArticles(): Flow<PagingData<NewsArticle>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { newsDao.getNewsArticles() }
        ).flow
    }

    fun getFavoriteArticles(): Flow<PagingData<NewsArticle>>  {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { newsDao.getFavoriteArticles() }
        ).flow
    }

    suspend fun addArticleToFavorites(article: NewsArticle) {
        article.isFavorite = true
        newsDao.addArticleToFavorites(article)
    }


    fun getNewsPagingSource(): PagingSource<Int, NewsArticle> {
        return newsDao.getNewsPagingSource()
    }

    suspend fun updateNewsArticle(newsArticle: NewsArticle) {
        newsDao.updateNewsArticle(newsArticle)
    }

    suspend fun removeFavoriteArticle(url: String) {
        newsDao.deleteByUrl(url)
    }

    suspend fun clearDatabase() {
        newsDao.clearAllNews()
    }
}
