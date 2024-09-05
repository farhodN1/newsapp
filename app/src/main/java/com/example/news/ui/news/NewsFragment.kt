package com.example.news.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.news.data.api.RetrofitInstance.api
import com.example.news.data.db.NewsDatabase
import com.example.news.data.repository.NewsRepository
import com.example.news.databinding.FragmentNewsBinding
import com.example.news.utils.DateTimeUtils.getRelativeTime

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val newsDao = NewsDatabase.getDatabase(requireContext()).getNewsDao()
        val newsRepository = NewsRepository(api, newsDao)
        val factory = NewsViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsUrl = arguments?.getString("url")

        if (newsUrl != null) {
            viewModel.loadNews(newsUrl).observe(viewLifecycleOwner, Observer { newsArticle ->
                newsArticle?.let {
                    binding.newsTitle.text = it.title
                    binding.newsPublishedAt.text = getRelativeTime(it.publishedAt)
                    binding.newsDescription.text = it.description
                    Glide.with(this)
                        .load(it.urlToImage)
                        .into(binding.newsImage)

                }
                binding.favoriteButton.setOnClickListener(){
                    if (newsArticle != null) {
                        viewModel.addArticleToFavorites(newsArticle)
                        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
