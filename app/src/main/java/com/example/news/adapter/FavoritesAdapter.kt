package com.example.news.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.data.db.NewsArticle
import com.example.news.databinding.ItemFavoritesBinding
import com.example.news.utils.DateTimeUtils.getRelativeTime
import com.example.news.utils.NewsDiffCallback

class FavoritesAdapter(
    private val onUnfavoriteClick: (String) -> Unit
) : PagingDataAdapter<NewsArticle, FavoritesAdapter.FavoritesViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    inner class FavoritesViewHolder(private val binding: ItemFavoritesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: NewsArticle) {
            Log.d("verification", article.isFavorite.toString())
            binding.newsTitle.text = article.title
            binding.publishedTime.text = getRelativeTime(article.publishedAt)
            Glide.with(binding.root)
                .load(article.urlToImage)
                .into(binding.newsImage)
            binding.deleteButton.apply {
                setOnClickListener { onUnfavoriteClick(article.url) }
            }
        }
    }
}
