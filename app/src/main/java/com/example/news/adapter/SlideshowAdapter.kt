package com.example.news.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.data.db.NewsArticle
import com.example.news.databinding.ItemSlideshowBinding
import com.example.news.utils.DateTimeUtils.getRelativeTime

class SlideshowAdapter(private val onFavoriteClicked: (NewsArticle) -> Unit, private val onItemClicked: (String) -> Unit) :
    ListAdapter<NewsArticle, SlideshowAdapter.SlideshowViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideshowViewHolder {
        val binding = ItemSlideshowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideshowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideshowViewHolder, position: Int) {
        val newsItem = getItem(position)
        holder.bind(newsItem)

        holder.itemView.setOnClickListener{
            onItemClicked(newsItem.url)
        }
    }

    inner class SlideshowViewHolder(private val binding: ItemSlideshowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsArticle) {
            Log.d("SlideshowAdapter", "Loading image from URL: ${newsItem.urlToImage}")

            // Bind your data here
            binding.slideshowTitle.text = newsItem.title
            binding.slideshowTime.text = getRelativeTime(newsItem.publishedAt)

            // Load the image with Glide
            Glide.with(binding.slideshowImage.context)
                .load(newsItem.urlToImage)
                .into(binding.slideshowImage)

            // Set up the favorite button click listener
            binding.favoriteButton.setOnClickListener {
                onFavoriteClicked(newsItem)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<NewsArticle>() {
        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem.url == newItem.url // or any unique ID
        }

        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem == newItem
        }
    }
}
