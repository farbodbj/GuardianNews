package com.bale_bootcamp.guardiannews.ui.news

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bale_bootcamp.guardiannews.R
import com.bale_bootcamp.guardiannews.databinding.NewsViewholderBinding
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class NewsAdapter(
    private val onItemClicked: (News) -> Unit
): PagingDataAdapter<News, NewsAdapter.NewsViewHolder>(DiffCallback) {
    private val TAG = "NewsAdapter"
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
        val viewHolder = NewsViewHolder(
            NewsViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        Log.d(TAG, "onCreateViewHolder: viewHolder created")

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            getItem(position)?.let { it1 -> onItemClicked(it1) }
        }
        Log.d(TAG, "onCreateViewHolder: viewHolder setOnClickListener")

        return viewHolder
    }


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        Log.d(TAG, "onBindViewHolder: viewHolder binded at position $position")
    }

    class NewsViewHolder(
        private var binding: NewsViewholderBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val TAG = "NewsViewHolder"
        fun bind(news: News) {
            bindTextual(news)
            bindImage(news)
            Log.d(TAG, "bind: viewHolder binded")
        }

        private fun bindTextual(news: News) = binding.apply {
            newsTitle.text = news.details.headline.parseAsHtml()
            newsSection.text = news.sectionName
            newsSummary.text = news.details.trailText.parseAsHtml()
            newsDate.text = news.webPublicationDate.toString().parseAsHtml()
            Log.d(TAG, "bindTextual: text binded")
        }

        private fun bindImage(news: News) = binding.apply {
            Glide.with(newsImage.context)
                .load(news.details.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.placeholder_square)
                .listener(object : RequestListener<Drawable> {
                    private val TAG = "Glide"
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e(TAG, "onLoadFailed: ${e?.message}")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "onResourceReady: image loaded")
                        return false
                    }
                })
                .into(newsImage)
            Log.d(TAG, "bindImage: image binded")
        }
    }
}


