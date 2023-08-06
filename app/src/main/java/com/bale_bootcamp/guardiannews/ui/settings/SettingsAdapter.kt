package com.bale_bootcamp.guardiannews.ui.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.bale_bootcamp.guardiannews.databinding.SettingsViewholderBinding
import com.bale_bootcamp.guardiannews.ui.settings.model.Setting

class SettingsAdapter (
    private val onItemClicked: (Setting) -> Unit
): ListAdapter<Setting, SettingsAdapter.SettingsViewHolder>(DiffCallback) {
    private val TAG = "SettingsAdapter"

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Setting>() {
            override fun areItemsTheSame(oldItem: Setting, newItem: Setting): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Setting, newItem: Setting): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val viewHolder = SettingsViewHolder(
            SettingsViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        Log.d(TAG, "view holder created")

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            getItem(position)?.let { it1 -> onItemClicked(it1) }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    class SettingsViewHolder(
        private var binding: SettingsViewholderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val TAG = "SettingsViewHolder"

        fun bind(setting: Setting) {

            binding.apply {
                Log.d(TAG, "bind: $setting")
                settingTitle.text = setting.title
                settingValue.text = setting.value
            }
        }
    }
}