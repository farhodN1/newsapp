package com.example.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.data.db.NewsArticle
import com.example.news.databinding.ItemNewsBinding

class SearchAdapter(private var articles: List<NewsArticle>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: NewsArticle) {
            // Bind data to your views here
            binding.newsTitle.text = article.title
            // Add any other data bindings here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    // Method to update the data
    fun updateData(newArticles: List<NewsArticle>) {
        articles = newArticles
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
