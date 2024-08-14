package com.example.neuigkeiten.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neuigkeiten.databinding.ItemNewsBinding
import com.example.neuigkeiten.data.db.NewsArticle

class NewsAdapter : PagingDataAdapter<NewsArticle, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {
    private var newsList: List<NewsArticle> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsArticle = getItem(position)
        if (newsArticle != null) {
            holder.bind(newsArticle)
        }
    }

    fun submitList(news: List<NewsArticle>) {
        newsList = news
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsArticle: NewsArticle) {
            Log.i("ulmita", newsArticle.publishedAt)
            binding.newsTitle.text = newsArticle.title
//            binding.publishedTime.text = newsArticle.publishedAt
            Glide.with(binding.newsImage.context).load(newsArticle.urlToImage).into(binding.newsImage)
        }
    }
}

class NewsDiffCallback : DiffUtil.ItemCallback<NewsArticle>() {
    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
        return oldItem == newItem
    }
}
