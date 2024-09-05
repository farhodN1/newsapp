package com.example.news.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.news.data.db.NewsArticle
import com.example.news.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    val favorites: Flow<PagingData<NewsArticle>> = repository.getFavoriteArticles()

    fun removeFromFavorites(url: String) {
        viewModelScope.launch {
            repository.removeFavoriteArticle(url)
        }
    }
}
class FavoritesViewModelFactory(
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}