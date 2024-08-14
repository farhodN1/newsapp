package com.example.neuigkeiten.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neuigkeiten.databinding.ItemSlideshowBinding
import com.example.neuigkeiten.data.db.NewsArticle

class SlideshowAdapter : RecyclerView.Adapter<SlideshowAdapter.SlideshowViewHolder>() {

    private var newsList: List<NewsArticle> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideshowViewHolder {
        val binding = ItemSlideshowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideshowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideshowViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun submitList(list: List<NewsArticle>) {
        newsList = list
        notifyDataSetChanged()
    }

    inner class SlideshowViewHolder(private val binding: ItemSlideshowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(newsArticle: NewsArticle) {
            Log.i("ultima", newsArticle.title.toString())
            binding.slideshowTitle.text = newsArticle.title
            // Again, assuming you have a method to load the image
            // Glide.with(binding.slideshowImage.context).load(newsArticle.urlToImage).into(binding.slideshowImage)
            Glide.with(binding.slideshowImage.context)
                .load(newsArticle.urlToImage)
                .into(binding.slideshowImage)
        }
    }
}
