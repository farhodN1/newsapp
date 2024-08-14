package com.example.neuigkeiten.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.neuigkeiten.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.example.neuigkeiten.data.db.NewsArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _selectedCategory = MutableStateFlow("All News")
    val selectedCategory: StateFlow<String> = _selectedCategory

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

//    val latestNews = newsRepository.getNewsArticles()
//        .flow
//        .cachedIn(viewModelScope)
    val latestNews: Flow<PagingData<NewsArticle>> = newsRepository.getNewsArticles()
        .cachedIn(viewModelScope)

    // Method to get slideshow data, you can define how to fetch it based on your needs
    val slideshowNews: Flow<List<NewsArticle>> = newsRepository.getSlideshowArticles();
    fun fetchNews() {
        viewModelScope.launch {

            val ok = newsRepository.fetchAndSaveNews("b6a9ffac377b457eb67d055eb0d8b17a", "US", 1)
            // Handle the result here
        }
    }

}
