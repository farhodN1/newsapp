package com.example.neuigkeiten.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neuigkeiten.R
import com.example.neuigkeiten.data.db.NewsArticle
import com.example.neuigkeiten.data.repository.NewsRepository
import kotlinx.coroutines.flow.collectLatest
import com.example.neuigkeiten.databinding.FragmentHomeBinding
import com.example.neuigkeiten.ui.adapters.NewsAdapter
import com.example.neuigkeiten.ui.adapters.SlideshowAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var selectedCategory: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.fetchNews()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSubmenu()


        // Setup ViewPager2 for Slideshow
        val slideshowAdapter = SlideshowAdapter()
        binding.viewPagerSlideshow.adapter = slideshowAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.slideshowNews.collectLatest { newsList ->
                slideshowAdapter.submitList(newsList)
            }
        }

        // Setup RecyclerView for Latest News
        val newsAdapter = NewsAdapter()
        binding.recyclerViewLatestNews.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLatestNews.adapter = newsAdapter
        lifecycleScope.launch {
            viewModel.latestNews.collectLatest { pagingData ->
                newsAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupSubmenu() {
        val defaultCategory = binding.btnAllNews
        selectCategory(defaultCategory)

        binding.btnAllNews.setOnClickListener { selectCategory(binding.btnAllNews) }
        binding.btnBusiness.setOnClickListener { selectCategory(binding.btnBusiness) }
        binding.btnPolitics.setOnClickListener { selectCategory(binding.btnPolitics) }
        binding.btnTech.setOnClickListener { selectCategory(binding.btnTech) }
        binding.btnScience.setOnClickListener { selectCategory(binding.btnScience) }
    }

    private fun selectCategory(selected: TextView) {
        // Reset the previous selection
        selectedCategory?.let {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.transparent
                )
            ) // Remove underline
            it.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.black
                )
            ) // Reset text color
            it.background = null // Remove drawable background
        }

        // Set the new selection
        selectedCategory = selected
        selectedCategory?.let {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.transparent
                )
            ) // Set underline color to transparent
            it.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.black
                )
            ) // Set text color
            it.setPadding(0, 0, 0, 0)
            it.setBackgroundResource(R.drawable.underline) // Set the underline drawable
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}