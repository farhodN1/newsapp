package com.example.neuigkeiten.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.neuigkeiten.data.api.NewsApi
import com.example.neuigkeiten.data.db.NewsDao
import com.example.neuigkeiten.data.db.NewsArticle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor(private val api: NewsApi, private val newsDao: NewsDao) {

    fun getSlideshowArticles(): Flow<List<NewsArticle>> {
        return newsDao.getTopNewsArticles(5)
    }

    fun getNewsArticles(): Flow<PagingData<NewsArticle>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { newsDao.getNewsArticles() }
        ).flow
    }

    fun getNewsPagingSource(): PagingSource<Int, NewsArticle> {
        return newsDao.getNewsPagingSource()
    }

    suspend fun fetchAndSaveNews(apiKey: String, country: String, page: Int) {
        val response = api.getTopHeadlines(country, page, apiKey)
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
                newsDao.insertAll(newsArticles)
            }
        }
    }

    fun searchNews(query: String): Pager<Int, NewsArticle> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { newsDao.searchNewsArticles("%$query%") }
        )
    }

    fun getFavoriteArticles(): Pager<Int, NewsArticle> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { newsDao.getFavoriteArticles() }
        )
    }


    suspend fun updateNewsArticle(newsArticle: NewsArticle) {
        newsDao.updateNewsArticle(newsArticle)
    }
}