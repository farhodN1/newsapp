package com.example.news.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.news.data.db.NewsArticle
import com.example.news.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel : ViewModel() {
    val newsRepository = NewsRepository()
    fun loadNews(url: String): LiveData<NewsArticle?> {
        val result = MutableLiveData<NewsArticle?>()
        viewModelScope.launch(Dispatchers.IO) {
            val article = newsRepository.getArticleByUrl(url)
            withContext(Dispatchers.Main) {
                result.value = article
            }
        }
        return result
    }
    fun addArticleToFavorites(article: NewsArticle) {
        viewModelScope.launch {
            newsRepository.addArticleToFavorites(article)
        }
    }
}