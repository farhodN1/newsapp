package com.example.news.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.news.data.api.RetrofitInstance
import com.example.news.data.db.NewsArticle

class SearchViewModel: ViewModel() {
    suspend fun getNews(searchTerm: String): List<NewsArticle>? {
        Log.d(searchTerm, searchTerm)
        val response = RetrofitInstance.api.searchNews(searchTerm, 2, "b6a9ffac377b457eb67d055eb0d8b17a")
        Log.i("status", response.isSuccessful.toString())
        return response.body()?.articles
    }
}