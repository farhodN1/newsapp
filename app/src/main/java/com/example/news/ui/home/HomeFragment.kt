package com.example.news.ui.home

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.news.R
import com.example.news.adapter.SlideshowAdapter
import com.example.news.adapters.NewsAdapter
import com.example.news.data.api.RetrofitInstance.api
import com.example.news.data.db.NewsDatabase
import com.example.news.data.repository.NewsRepository
import com.example.news.databinding.FragmentHomeBinding
import com.example.news.utils.NetworkUtils.isInternetAvailable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Instantiate the database and repository
        val newsDao = NewsDatabase.getDatabase(requireContext()).getNewsDao()
        val newsRepository = NewsRepository(api, newsDao)
        val factory = HomeViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val submenuContainer = binding.submenuContainer

        // Set OnClickListener to each TextView inside the LinearLayout
        for (i in 0 until submenuContainer.childCount) {
            val textView = submenuContainer.getChildAt(i) as? TextView
            textView?.setOnClickListener { clickedTextView ->
                // Apply underline to the clicked TextView
                underlineTextView(clickedTextView as TextView)

                // Optionally, remove underline from other TextViews
                removeUnderlineFromOthers(submenuContainer, clickedTextView)
            }
        }

        setupSlideShow()
        if(isInternetAvailable(requireContext())) viewModel.fetchNews()

        // NewsAdapter setup
        val newsAdapter = NewsAdapter(

            onFavoriteClicked = { article ->
                viewModel.addArticleToFavorites(article)
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
            },onItemClicked = { articleUrl ->
                // Handle navigation to NewsFragment
                val bundle = Bundle().apply {
                    putString("url", articleUrl)
                }
                findNavController().navigate(R.id.navigation_news, bundle)
            }
        )

        binding.recyclerViewLatestNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }

        lifecycleScope.launch {
            val result = api.searchNews("tesla", 2, "b6a9ffac377b457eb67d055eb0d8b17a")
            Log.i("result", result.toString())
            viewModel.latestNews.collectLatest { pagingData ->
                newsAdapter.submitData(lifecycle, pagingData)
            }
        }
    }

    private fun underlineTextView(textView: TextView) {
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun removeUnderlineFromOthers(container: LinearLayout, currentTextView: TextView) {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i) as? TextView
            if (child != null && child != currentTextView) {
                child.paintFlags = child.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
        }
    }

    private fun setupSlideShow() {
        val viewPager: ViewPager2 = binding.viewPagerSlideshow
        val adapter = SlideshowAdapter(

            onFavoriteClicked = { article ->
                viewModel.addArticleToFavorites(article)
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
            },onItemClicked = { articleUrl ->
                val bundle = Bundle().apply {
                    putString("url", articleUrl)
                }
                findNavController().navigate(R.id.navigation_news, bundle)
            }
        )
        viewPager.adapter = adapter

        // Observe and submit the slideshow data
        viewModel.slideshowData.observe(viewLifecycleOwner) { newsList ->
            adapter.submitList(newsList)
        }
        // Carousel effect with zoom and partial visibility
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(0))  // Adjust margin between items
            addTransformer { page, position ->
                val scaleFactor = 0.90f + (1 - abs(position)) * 0.10f
                page.scaleY = scaleFactor
                page.scaleX = scaleFactor
            }
        }
        viewPager.setPageTransformer(transformer)

        // Adjusting offscreenPageLimit and padding for partial visibility
        viewPager.offscreenPageLimit = 3
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.setPadding(60, 0, 60, 0)  // Adjust padding for edge visibility

        startAutoSliding()
    }

    private fun startAutoSliding() {
        val runnable = object : Runnable {
            override fun run() {
                val nextItem = (binding.viewPagerSlideshow.currentItem + 1) % (binding.viewPagerSlideshow.adapter?.itemCount ?: 1)
                binding.viewPagerSlideshow.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000) // Change slide every 3 seconds
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

}