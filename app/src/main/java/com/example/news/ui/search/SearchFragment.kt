package com.example.news.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapter.SearchAdapter
import com.example.news.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = binding.recyclerSearch
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set up the click listener for the search button
        binding.ivSearch.setOnClickListener {
            val searchTerm = binding.etSearch.text.toString()

            // Set an empty adapter before fetching data
            val adapter = SearchAdapter(emptyList())
            recyclerView.adapter = adapter

            // Fetch the news articles and update the adapter
            viewLifecycleOwner.lifecycleScope.launch {
                val articles = viewModel.getNews(searchTerm)
                articles?.let {
                    adapter.updateData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
