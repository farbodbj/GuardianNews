package com.bale_bootcamp.guardiannews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bale_bootcamp.guardiannews.databinding.NewsViewholderBinding
import com.bale_bootcamp.guardiannews.model.News
import com.bumptech.glide.Glide

class NewsAdapter(
    private val onItemClicked: (News) -> Unit
): ListAdapter<News, NewsAdapter.NewsViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val viewHolder: NewsViewHolder = NewsViewHolder(
            NewsViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener() {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class NewsViewHolder(
        private var binding: NewsViewholderBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) {
            bindTextual(news)
            bindImage(news)
        }

        private fun bindTextual(news: News) = binding.apply {
            newsTitle.text = news.details.headline
            newsSummary.text = news.details.trailText
            newsDate.text = news.webPublicationDate.toString()
        }

        private fun bindImage(news: News) = binding.apply {
            Glide.with(newsImage.context)
                .load(news.details.thumbnail)
                .centerCrop()
                .into(newsImage)
        }
    }
}



