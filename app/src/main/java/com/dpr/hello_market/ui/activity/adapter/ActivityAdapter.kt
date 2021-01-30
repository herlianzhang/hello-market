package com.dpr.hello_market.ui.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpr.hello_market.databinding.ItemActivityBinding
import com.dpr.hello_market.db.activity.ActivityDbModel

class ActivityAdapter : ListAdapter<ActivityDbModel, ActivityAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ActivityDbModel) {

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemActivityBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ActivityDbModel>() {
        override fun areItemsTheSame(oldItem: ActivityDbModel, newItem: ActivityDbModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ActivityDbModel,
            newItem: ActivityDbModel
        ): Boolean =
            oldItem == newItem
    }
}