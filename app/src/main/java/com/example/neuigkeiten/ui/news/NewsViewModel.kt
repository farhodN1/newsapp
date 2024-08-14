package com.example.neuigkeiten.ui.news

import androidx.lifecycle.ViewModel
import com.example.neuigkeiten.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    // Implement logic to get and manage news details
}