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

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

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
class NewsViewModelFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
