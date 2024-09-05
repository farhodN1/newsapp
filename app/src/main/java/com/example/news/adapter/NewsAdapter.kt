package com.example.news.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.databinding.ItemNewsBinding
import com.example.news.data.db.NewsArticle
import com.example.news.utils.NewsDiffCallback
import com.example.news.utils.DateTimeUtils.getRelativeTime

class NewsAdapter(private val onFavoriteClicked: (NewsArticle) -> Unit, private val onItemClicked: (String) -> Unit): PagingDataAdapter<NewsArticle, NewsAdapter.NewsViewHolder>(
    NewsDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsArticle = getItem(position)
        if (newsArticle != null) {
            // Log each article being bound to the RecyclerView
            Log.d("RecyclerViewBind", "Binding article at position $position: $newsArticle")
            holder.bind(newsArticle)

            holder.itemView.setOnClickListener{
                onItemClicked(newsArticle.url)
            }
        }
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsArticle: NewsArticle) {
            binding.newsTitle.text = newsArticle.title
            binding.publishedTime.text = getRelativeTime(newsArticle.publishedAt)
            Glide.with(binding.newsImage.context)
                .load(newsArticle.urlToImage)
                .into(binding.newsImage)
            binding.favoriteButton.setOnClickListener{
                onFavoriteClicked(newsArticle)
                }
        }
    }


}


