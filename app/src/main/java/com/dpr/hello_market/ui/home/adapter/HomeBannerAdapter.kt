package com.dpr.hello_market.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dpr.hello_market.databinding.ItemBannerBinding
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.vo.Banner

class HomeBannerAdapter : ListAdapter<Banner, HomeBannerAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Banner) {
            binding.executePendingBindings()

            Helper.setImage(item, binding.ivBanner)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemBannerBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Banner>() {
        override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean =
            oldItem == newItem
    }
}