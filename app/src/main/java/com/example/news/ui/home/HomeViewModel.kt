package com.example.news.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.data.db.NewsArticle
import com.example.news.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val slideshowData: LiveData<List<NewsArticle>> = newsRepository.getSlideshowItems()
    val latestNews: Flow<PagingData<NewsArticle>> = newsRepository.getNewsArticles()
        .cachedIn(viewModelScope)

    fun addArticleToFavorites(article: NewsArticle) {
        viewModelScope.launch {
            newsRepository.addArticleToFavorites(article)
        }
    }

    fun fetchNews(category: String) {
        viewModelScope.launch {
            newsRepository.fetchAndSaveNews(category)
        }
    }

    suspend fun clearDatabase(){
        newsRepository.clearDatabase()
    }
}

class HomeViewModelFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
