package com.example.news.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapter.FavoritesAdapter
import com.example.news.data.api.RetrofitInstance
import com.example.news.data.db.NewsDatabase
import com.example.news.data.repository.NewsRepository
import com.example.news.databinding.FragmentFavoritesBinding
import com.example.news.ui.home.HomeViewModel
import com.example.news.ui.home.HomeViewModelFactory
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var viewModel: FavoritesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val newsDao = NewsDatabase.getDatabase(requireContext()).getNewsDao()
        val newsRepository = NewsRepository(RetrofitInstance.api, newsDao)

        // Create the ViewModel using the factory
        val factory = FavoritesViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, factory).get(FavoritesViewModel::class.java)
        _binding = null
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesAdapter = FavoritesAdapter { url ->
            viewModel.removeFromFavorites(url)
        }

        binding.recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }

        lifecycleScope.launch {
            viewModel.favorites.collect { pagingData ->
                favoritesAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
